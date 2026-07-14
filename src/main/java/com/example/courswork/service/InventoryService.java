package com.example.courswork.service;

import com.example.courswork.model.part;
import com.example.courswork.parser.InventoryParser;
import com.example.courswork.util.AuditLogger;
import java.util.ArrayList;
import java.util.List;

public class InventoryService {

    private List<part> parts = new ArrayList<>();
    private final AuditLogger auditLogger;

    public InventoryService(AuditLogger auditLogger) {
        this.auditLogger = auditLogger;
    }

    public void loadFromFile(String filePath) {
        parts = InventoryParser.parseParts(filePath);
    }

    public void saveToFile(String filePath) {

    }

    public boolean addPart(part newPart) {
        return false;
    }

    public boolean deletePart(String partId) {
        return false;
    }

    public boolean updatePart(String partId, part updatedPart) {
        return false;
    }

    public List<part> getAllParts() {
        return parts;
    }

    public List<part> getGroupedAndSortedInventory() {
        return null;
    }

    public int getTotalItemCount() {
        return 0;
    }

    public double getTotalInventoryValue() {
        return 0.0;
    }
}