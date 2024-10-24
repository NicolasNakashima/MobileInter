package com.example.khiata.models;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private int typeId;
    private String dressMarkerName;
    private double avaliation;
    private String description;
    private String size;

    public Product(int id, String name, double price, String imageUrl, int typeId, String dressMarkerName, double avaliation, String description, String size) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.typeId = typeId;
        this.dressMarkerName = dressMarkerName;
        this.avaliation = avaliation;
        this.description = description;
        this.size = size;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public String getDressMarkerName() {
        return dressMarkerName;
    }

    public void setDressMarkerName(String dressMarkerName) {
        this.dressMarkerName = dressMarkerName;
    }

    public double getAvaliation() {
        return avaliation;
    }

    public void setAvaliation(double avaliation) {
        this.avaliation = avaliation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUrl='" + imageUrl + '\'' +
                ", typeId=" + typeId +
                ", dressMarkerName='" + dressMarkerName + '\'' +
                ", avaliation=" + avaliation +
                ", description='" + description + '\'' +
                ", size='" + size + '\'' +
                '}';
    }
}
