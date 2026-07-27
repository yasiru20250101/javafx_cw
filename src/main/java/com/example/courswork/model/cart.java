package com.example.courswork.model;

import com.example.courswork.service.InventoryService;
import com.example.courswork.util.AuditLogger;
import java.util.ArrayList;
import java.util.List;

public class cart {

    private List<cartitem> items = new ArrayList<>();

    public List<cartitem> getItems() {
        return items;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }


    public boolean addItem(part part, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        if (quantity > part.getQuantity()) {
            return false;
        }
        for (cartitem item : items) {
            if (item.getPart().getPartid().equals(part.getPartid())) {
                item.setQuantity(item.getQuantity() + quantity);
                return true;
            }
        }
        items.add(new cartitem(part, quantity));
        return true;
    }

    public boolean removeItem(String partId) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getPart().getPartid().equals(partId)) {
                items.remove(i);
                return true;
            }
        }
        return false;
    }


    private double getItemTotalAfterBulkDiscount(cartitem item) {
        if (item.getQuantity() >= 3) {
            return item.getTotal() * 0.95;
        }
        return item.getTotal();
    }


    private boolean qualifiesForSynergyDiscount() {
        boolean hasEngine = false;
        boolean hasElectrical = false;

        for (cartitem item : items) {
            String category = item.getPart().getCategory().toLowerCase();
            if (category.equals("engine")) {
                hasEngine = true;
            }
            if (category.equals("electrical")) {
                hasElectrical = true;
            }
        }
        return hasEngine && hasElectrical;
    }


    public double calculateTotal() {
        double total = 0.0;
        for (cartitem item : items) {
            total += getItemTotalAfterBulkDiscount(item);
        }
        if (qualifiesForSynergyDiscount()) {
            total = total * 0.90;
        }
        return total;
    }

    public String checkout(InventoryService inventoryService) {
        if (items.isEmpty()) {
            return "Cart is empty. Nothing to checkout.";
        }

        double total = calculateTotal();
        StringBuilder receipt = new StringBuilder();
        receipt.append("Checkout complete.\n");

        for (cartitem item : items) {
            part p = item.getPart();
            int newQuantity = p.getQuantity() - item.getQuantity();

            part updatedPart = new part(
                    p.getPartid(),
                    p.getPartname(),
                    p.getBrand(),
                    p.getPrice(),
                    newQuantity,
                    p.getCategory(),
                    p.getDateAdded(),
                    p.getImage()
            );

            inventoryService.updatePart(p.getPartid(), updatedPart);

            AuditLogger.log(inventoryService.auditLogPath, "STOCK_DEDUCT", p.getPartid(), "-" + item.getQuantity());

            receipt.append(p.getPartname()).append(" x").append(item.getQuantity()).append("\n");
        }

        AuditLogger.log(inventoryService.auditLogPath, "CHECKOUT", "-", "Total: Rs. " + String.format("%.2f", total));

        receipt.append("Total: Rs. ").append(String.format("%.2f", total));

        items.clear();

        return receipt.toString();
    }
}