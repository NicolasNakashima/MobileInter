package com.example.khiata.models;

public class Address {
    private long id=0;
    private String street;
    private int number;
    private String complement;
    private String label;

    public Address(long id, String street, int number, String complement, String label) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.label = label;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", complement='" + complement + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
