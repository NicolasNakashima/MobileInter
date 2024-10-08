package com.example.khiata.models;

import java.util.List;

public class User {
    private String name;
    private String cpf;
    private int genderId;
    private int age;
    private boolean isDressmaker;
    private boolean isPremium; //Deixa null, porque ele se torna premium dentro do App
    private int phone;
    private String imageURL; //Deixa null
    private String password;
    private String email;
    private String profilePictureUrl; //Deixa null
    private List<Address> addresses; //Deixa uma lista vázia para cadastrar

    // Constructor
    public User(String name, String cpf, int genderId, int age, boolean isDressmaker,
                boolean isPremium, int phone, String imageURL, String password,
                String email, String profilePictureUrl, List<Address> addresses) {
        this.name = name;
        this.cpf = cpf;
        this.genderId = genderId;
        this.age = age;
        this.isDressmaker = isDressmaker;
        this.isPremium = isPremium;
        this.phone = phone;
        this.imageURL = imageURL;
        this.password = password;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.addresses = addresses;
    }

    // Getters and Setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public int getGenderId() {
        return genderId;
    }

    public void setGenderId(int genderId) {
        this.genderId = genderId;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public boolean isDressmaker() {
        return isDressmaker;
    }

    public void setDressmaker(boolean dressmaker) {
        isDressmaker = dressmaker;
    }

    public boolean isPremium() {
        return isPremium;
    }

    public void setPremium(boolean premium) {
        isPremium = premium;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getProfilePictureUrl() {
        return profilePictureUrl;
    }

    public void setProfilePictureUrl(String profilePictureUrl) {
        this.profilePictureUrl = profilePictureUrl;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", genderID=" + genderId +
                ", age=" + age +
                ", isDressmaker=" + isDressmaker +
                ", isPremium=" + isPremium +
                ", phone=" + phone +
                ", imageURL='" + imageURL + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", addresses=" + addresses + '\'' +
                '}';
    }
}
