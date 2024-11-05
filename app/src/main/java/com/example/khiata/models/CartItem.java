package com.example.khiata.models;

public class CartItem {
    //Carmpos
    private String name;
    private String quantity;

    //Constructor
    public CartItem(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    //Getters and Setters
    public String getName() {
        return name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
