package com.example.khiata.models;

import com.example.khiata.models.Gender;
public class User {
    private int id;
    private String name;
    private String cpf;
    private Gender gender;
    private int age;
    private boolean isDressmaker;
    private boolean isPremium;
    private long phone;
    private String imageURL;
    private String password;
    private String email;
    private String profilePictureUrl;

    // Constructor
    public User(int id, String name, String cpf, Gender gender, int age, boolean isDressmaker,
                boolean isPremium, long phone, String imageURL, String password,
                String email, String profilePictureUrl) {
        this.id = id;
        this.name = name;
        this.cpf = cpf;
        this.gender = gender;
        this.age = age;
        this.isDressmaker = isDressmaker;
        this.isPremium = isPremium;
        this.phone = phone;
        this.imageURL = imageURL;
        this.password = password;
        this.email = email;
        this.profilePictureUrl = profilePictureUrl;
    }

    // Getters and Setters
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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
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

    public long getPhone() {
        return phone;
    }

    public void setPhone(long phone) {
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

    // Gender class

}
