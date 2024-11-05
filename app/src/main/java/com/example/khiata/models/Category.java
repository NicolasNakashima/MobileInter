package com.example.khiata.models;

public class Category {
    //Campos
    private int id;
    private String type;

    //Constructor
    public Category(int id, String type) {
        this.id = id;
        this.type = type;
    }

    //Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    //toString
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
