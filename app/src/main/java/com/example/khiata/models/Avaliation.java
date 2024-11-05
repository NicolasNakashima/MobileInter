package com.example.khiata.models;

public class Avaliation {
    //Campos
    private int id;
    private String userName;
    private String comment;
    private double rating;

    //Construtor
    public Avaliation(String userName, String comment, double rating) {
        this.userName = userName;
        this.comment = comment;
        this.rating = rating;
    }

    //Getters e Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    //ToString
    @Override
    public String toString() {
        return "Avaliation{" +
                "id=" + id + '\'' +
                "userName='" + userName + '\'' +
                ", comment='" + comment + '\'' +
                ", rating=" + rating +
                '}';
    }
}
