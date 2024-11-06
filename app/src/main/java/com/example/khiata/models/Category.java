package com.example.khiata.models;

public class Category {
    //Campos
    private int id;
    private String category;

    //Constructor
    public Category(int id, String category) {
        this.id = id;
        this.category = category;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    //toString
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", category='" + category + '\'' +
                '}';
    }
}
