package com.example.khiata.models;

public class Address {
    private int id;
    private String recipient;
    private String street;
    private int number;
    private String complement;
    private String label;

    public Address(String recipient ,String street, int number, String complement, String label) {
        this.recipient = recipient;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.label = label;
    }

    public Address(int id,String recipient ,String street, int number, String complement, String label) {
        this.id = id;
        this.recipient = recipient;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }
    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getComplement() {
        return complement;
    }

    public void setComplement(String complement) {
        this.complement = complement;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", recipient='" + recipient + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", complement='" + complement + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
