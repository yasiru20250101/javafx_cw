package com.example.courswork.model;

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

    // Done — we wrote this together already
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


    private double getItemTotalAfterBulkDiscount(cartitem item) {
        return 0.0;
    }


    private boolean qualifiesForSynergyDiscount() {
        return false;
    }


    public double calculateTotal() {
        return 0.0;
    }

    public String checkout() {
        return null;
    }
}