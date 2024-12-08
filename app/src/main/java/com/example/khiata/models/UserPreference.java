package com.example.khiata.models;

public class UserPreference {
    //Campos
    private int pfk_user_id;
    private String value;

    //Constructor
    public UserPreference(int pfk_user_id, String value) {
        this.pfk_user_id = pfk_user_id;
        this.value = value;
    }

    //Getters and Setters
    public int getPfk_user_id() {
        return pfk_user_id;
    }

    public void setPfk_user_id(int pfk_user_id) {
        this.pfk_user_id = pfk_user_id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    //toString
    @Override
    public String toString() {
        return "UserPreference{" +
                "pfk_user_id=" + pfk_user_id +
                ", value='" + value + '\'' +
                '}';
    }
}
