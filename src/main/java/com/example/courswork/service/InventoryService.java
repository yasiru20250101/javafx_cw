package com.example.courswork.service;

import com.example.courswork.model.part;
import com.example.courswork.parser.InventoryParser;
import com.example.courswork.util.AuditLogger;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {
    public List<part> parts;
    public String filePath;
    public String auditLogPath;
    public int lowStockThreshold;

    public InventoryService(String filePath, String auditLogPath, int lowStock ) {
        this.filePath = filePath;
        this.auditLogPath = auditLogPath;
        this.lowStockThreshold = lowStock;
        this.parts = new ArrayList<>();
    }

    public void loadParts() {
        parts = InventoryParser.parseParts(filePath);

    }
    public void addPart(part part) {
        parts.add(part);
        saveToFile();
        AuditLogger.log(auditLogPath,"ADD",part.getPartid(),"Qty: " + part.getQuantity());

    }
    public void deletePart(String partId) {
        part toDelete = null;

        for (int i = 0; i < parts.size() ; i++) {
            if(parts.get(i).getPartid().equals(partId)) {
                toDelete = parts.get(i);
                break;

            }
        }
        if(toDelete != null) {
            parts.remove(toDelete);
            saveToFile();
            AuditLogger.log(auditLogPath, "DELETE" , partId, "-");

        }

    }
    public void updatePart(String partId, part updatedPart) {
        for (int i = 0; i < parts.size(); i ++) {
            if(parts.get(i).getPartid().equals(partId)) {
                parts.set(i,updatedPart);
                saveToFile();
                return;
            }

        }
    }
    public List<part> searchParts(String keyword, String category, double minPrice, double maxPrice) {
        List<part> results = new ArrayList<>();

        for (int i = 0; i < parts.size(); i++) {
            part part = parts.get(i);

            boolean matchesKeyword = keyword.isEmpty() || part.getPartname().toLowerCase().contains(keyword.toLowerCase()) ||
                    part.getBrand().toLowerCase().contains(keyword.toLowerCase()) || part.getPartid().toLowerCase().contains(keyword.toLowerCase());

            boolean matchesCategory = category.isEmpty() || part.getCategory().toLowerCase().equals(category.toLowerCase());

            boolean matchesPrice = part.getPrice() >= minPrice && (maxPrice == 0 || part.getPrice() <= maxPrice);

            if (matchesKeyword && matchesCategory && matchesPrice) {
                results.add(part);
            }
        }
        return results;

    }
    public List<part> getLowStockParts() {
        List<part> lowStock = new ArrayList<>();

        for (int i = 0; i < parts.size(); i++) {
            if(parts.get(i).getQuantity() <= lowStockThreshold) {
                lowStock.add(parts.get(i));
            }
        }
        return lowStock;
    }
    public List<part> getSortedParts() {
        List<part> sorted = new ArrayList<>(parts);

        for (int i = 0; i < sorted.size()  -1; i++) {
            for (int j = 0; j < sorted.size()-i-1; j++) {
                part a = sorted.get(j);
                part b = sorted.get(j+1);

                int categoryCompare = a.getCategory().toLowerCase() .compareTo(b.getCategory().toLowerCase());

                if (categoryCompare > 0) {
                    sorted.set(j,b);
                    sorted.set(j+1,a);
                }else if (categoryCompare == 0) {
                    if (a.partId.compareTo(b.partId) > 0) {
                        sorted.set(j,b);
                        sorted.set(j+1,a);

                    }
                }
            }
        }
        return sorted;
    }
    public void saveToFile() {
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));

            for (int i = 0; i < parts.size() ; i++) {
                writer.write(parts.get(i).toString());
                writer.newLine();

            }
            writer.close();

        } catch (IOException e) {
            System.out.println("Error saving file: " + e.getMessage());
        }
    }
    public double getTotalValue() {
        double total = 0;

        for (int i = 0; i < parts.size(); i++) {
            total += parts.get(i).price * parts.get(i).quantity;

        }
        return total;
    }
    public int getTotalCount() {
        return parts.size();
    }

}
