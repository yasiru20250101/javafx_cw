package com.example.courswork.model;

public class cartitem {

    private part part;
    private int quantity;

    public cartitem(part part, int quantity) {
        this.part = part;
        this.quantity = quantity;
    }

    public part getPart() {
        return part;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return part.getPrice() * quantity;
    }

    @Override
    public String toString() {
        return part.getPartname() + " * " + quantity + " = Rs. " + getTotal();
    }
}