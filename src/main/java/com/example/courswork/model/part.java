package com.example.courswork.model;

public class part {

    private String partid;
    private String partname;
    private String brand;
    double price;
    private int quantity;
    private String category;
    private String dateAdded;
    private String image;

    public part(String partid, String partname, String brand,double price, int quantity,String category,String dateAdded, String image){
        this.partid = partid;
        this.partname = partname;
        this.brand = brand;
        this.price = price;
        this.quantity = quantity;
        this.category = category;
        this.dateAdded = dateAdded;
        this.image = image;
    }

    public String toString() {
        return partid + " | " + partname + " | " + brand + " | Rs." + price + " | Qty: " + quantity + " | Category: " + category + " | Date Added: " + dateAdded + " | Image: " + image;
    }
}