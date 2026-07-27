package com.example.courswork.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CartTest {

    private cart cart;
    private part enginePart;
    private part electricalPart;
    private part brakePart;

    @BeforeEach
    void setUp() {
        cart = new cart();
        enginePart = new part("P001", "Piston", "Bajaj", 1000.0, 10, "engine", "2023-10-12", "piston.jpg");
        electricalPart = new part("P002", "Spark Plug", "NGK", 500.0, 10, "electrical", "2023-10-12", "plug.jpg");
        brakePart = new part("P003", "Brake Pad", "TVS", 200.0, 10, "brakes", "2023-10-12", "pad.jpg");
    }

    @Test
    void testInvalidInputHandling() {
        // Zero or negative quantity inputs
        assertFalse(cart.addItem(enginePart, 0));
        assertFalse(cart.addItem(enginePart, -3));

        // Exceeding available stock
        assertFalse(cart.addItem(enginePart, 15));

        assertTrue(cart.isEmpty());
    }

    @Test
    void testStandardCalculationNoDiscount() {
        cart.addItem(enginePart, 2); // 2000.0
        cart.addItem(brakePart, 1);  // 200.0

        assertEquals(2200.0, cart.calculateTotal(), 0.001);
    }

    @Test
    void testBulkDiscountRule() {
        // Bulk Discount: 5% off item line when quantity >= 3
        cart.addItem(brakePart, 3); // 3 * 200 = 600.0 -> 5% off = 570.0

        assertEquals(570.0, cart.calculateTotal(), 0.001);
    }

    @Test
    void testCategorySynergyDiscountRule() {
        // Synergy Discount: 10% off cart total if both 'engine' and 'electrical' parts exist
        cart.addItem(enginePart, 1);     // 1000.0
        cart.addItem(electricalPart, 1); // 500.0
        // Total before synergy = 1500.0 -> 10% off = 1350.0

        assertEquals(1350.0, cart.calculateTotal(), 0.001);
    }

    @Test
    void testCombinedDiscounts() {
        // Bulk discount on electricalPart (qty 3) + Synergy discount (engine + electrical present)
        cart.addItem(enginePart, 1);     // 1000.0
        cart.addItem(electricalPart, 3); // 3 * 500 = 1500 -> 5% off bulk = 1425.0
        // Subtotal = 2425.0 -> Synergy 10% off = 2182.5

        assertEquals(2182.5, cart.calculateTotal(), 0.001);
    }
}