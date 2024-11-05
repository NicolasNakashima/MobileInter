package com.example.khiata.models;

import java.util.List;

public class Cart {
    //Campos
    private List<CartItem> items;
    private String total;

    //Constructor
    public Cart(List<CartItem> items, String total) {
        this.items = items;
        this.total = total;
    }

    //Getters and Setters
    public List<CartItem> getItems() {
        return items;
    }

    public String getTotal() {
        return total;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
