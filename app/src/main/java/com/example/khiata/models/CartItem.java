package com.example.khiata.models;

public class CartItem {
    private String name;
    private String quantity;

    // Construtor
    public CartItem(String name, String quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }
}
