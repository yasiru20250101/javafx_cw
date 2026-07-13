package com.example.courswork.model;

public class part {

    private String partid;
    private String partname;
    private String brand;
    private double price;
    private int quantity;
    private String category;
    private String dateAdded;
    private String image;

    public part(String partid, String partname, String brand, double price,
                int quantity, String category, String dateAdded, String image) {

        this.partid = partid;
        this.partname = partname;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.image = image;
    }

    // Getters
    public String getPartid() {
        return partid;
    }

    public String getPartname() {
        return partname;
    }

    public String getBrand() {
        return brand;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public String getImage() {
        return image;
    }


    public String toString() {
        return partid + " | " +
                partname + " | " +
                brand + " | Rs. " +
                price + " | Qty: " +
                quantity + " | Category: " +
                category + " | Date Added: " +
                dateAdded + " | Image: " +
                image;
    }
}