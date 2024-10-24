package com.example.khiata.models;

import java.util.List;

public class User {
    private int id;
    private String name;
    private String cpf;
    private int genderId;
    private int age;
    private boolean isDressmaker;
    private int premiumStatus; //Deixa 0 no cadastro, (0 = normal, 1 = premium, 2 = pendente)
    private String phones;
    private String imageURL; //Deixa null no cadastro
    private String password;
    private String email;
    private String profilePictureUrl; //Deixa null no cadastro
    private List<Address> addresses;

    // Constructor
    public User(String name, String cpf, int genderId, int age, boolean isDressmaker,
                int premiumStatus, String phones, String imageURL, String password,
                String email, String profilePictureUrl) {
        this.name = name;
        this.cpf = cpf;
        this.genderId = genderId;
        this.age = age;
        this.isDressmaker = isDressmaker;
        this.premiumStatus = premiumStatus;
        this.phones = phones;
        this.imageURL = imageURL;
        this.password = password;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    public User(String name, String cpf, int genderId, int age, boolean isDressmaker, int premiumStatus, String phones, String imageURL, String password, String email, String profilePictureUrl, List<Address> addresses) {
        this.name = name;
        this.cpf = cpf;
        this.genderId = genderId;
        this.age = age;
        this.isDressmaker = isDressmaker;
        this.premiumStatus = premiumStatus;
        this.phones = phones;
        this.imageURL = imageURL;
        this.password = password;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
        this.addresses = addresses;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getPremiumStatus() {
        return premiumStatus;
    }

    public void setPremiumStatus(int premiumStatus) {
        premiumStatus= premiumStatus;
    }

    public String getPhone() {return phones;}

    public void setPhone(String phones) {
        this.phones = phones;
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
                ", premiumStatus=" + premiumStatus +
                ", phone=" + phones +
                ", imageURL='" + imageURL + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", profilePictureUrl='" + profilePictureUrl + '\'' +
                ", addresses=" + addresses + '\'' +
                '}';
    }
}
