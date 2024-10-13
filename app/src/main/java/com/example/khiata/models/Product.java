package com.example.khiata.models;

public class Product {
    private int id;
    private String name;
    private double price;
    private String imageUrl;
    private int typeId;
    private String dressMarkerName;
    private double avaliation;

    public Product(int id, String name, double price, String imageUrl, int typeId, String dressMarkerName, double avaliation) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.typeId = typeId;
        this.dressMarkerName = dressMarkerName;
        this.avaliation = avaliation;
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
                '}';
    }
}
