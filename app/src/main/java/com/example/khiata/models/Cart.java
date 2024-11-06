package com.example.khiata.models;

import java.util.List;

public class Cart {
    private List<CartItem> items;
    private String cartId;
    private String total;

    // Construtor
    public Cart(List<CartItem> items, String cartId, String total) {
        this.items = items;
        this.cartId = cartId;
        this.total = total;
    }

    // Getters e Setters
    public List<CartItem> getItems() {
        return items;
    }

    public void setItems(List<CartItem> items) {
        this.items = items;
    }

    public String getCartId() {
        return cartId;
    }

    public void setCartId(String cartId) {
        this.cartId = cartId;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }
}
