package com.example.courswork.util;

import com.example.courswork.model.cart;
import com.example.courswork.model.part;
import com.example.courswork.service.InventoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class AuditLoggerTest {

    @TempDir
    Path tempDir;

    private File tempAuditFile;
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() throws IOException {
        File tempInventoryFile = tempDir.resolve("inventory.txt").toFile();
        tempAuditFile = tempDir.resolve("audit.txt").toFile();

        String data = "P001, Bajaj Piston, Bajaj, Rs. 1000.0, 10, engine, 2023-10-12, piston.jpg\n";
        Files.write(tempInventoryFile.toPath(), data.getBytes(StandardCharsets.UTF_8));

        inventoryService = new InventoryService(tempInventoryFile.getAbsolutePath(), tempAuditFile.getAbsolutePath(), 5);
        inventoryService.loadParts();
    }

    @Test
    void testAuditLoggerDirectCall() throws IOException {
        AuditLogger.log(tempAuditFile.getAbsolutePath(), "ADD", "P001", "Qty: 10");

        String content = new String(Files.readAllBytes(tempAuditFile.toPath()), StandardCharsets.UTF_8);
        assertTrue(content.contains("ADD"));
        assertTrue(content.contains("P001"));
        assertTrue(content.contains("Qty: 10"));
    }

    @Test
    void testCheckoutStockDeductionAndAuditLogging() throws IOException {
        cart cart = new cart();
        part p = inventoryService.parts.get(0);

        cart.addItem(p, 4);
        String receipt = cart.checkout(inventoryService);

        // Verify receipt summary
        assertTrue(receipt.contains("Checkout complete."));
        assertTrue(cart.isEmpty());

        // Verify quantity updated in inventory memory
        assertEquals(6, inventoryService.parts.get(0).getQuantity());

        // Verify audit logs were written to file
        String auditContent = new String(Files.readAllBytes(tempAuditFile.toPath()), StandardCharsets.UTF_8);
        assertTrue(auditContent.contains("STOCK_DEDUCT"));
        assertTrue(auditContent.contains("CHECKOUT"));
        assertTrue(auditContent.contains("P001"));
        assertTrue(auditContent.contains("-4"));
    }
}