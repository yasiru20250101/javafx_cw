package com.example.courswork.model;

public class cartItem {
    private part part;
    private int quantity;

    public cartItem(part part,int quantity){
        this.part=part;
        this.quantity=quantity;
    }
    public double getTotal(){
        return part.price *quantity;
    }

    @Override
    public String toString() {
        return part.partname + "*" +quantity +"RS."+getTotal();
    }
}
