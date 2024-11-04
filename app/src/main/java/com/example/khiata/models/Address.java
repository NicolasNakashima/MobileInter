package com.example.khiata.models;

public class Address {
    private int id;
    private String state;
    private String country;
    private String street;
    private int number;
    private String complement;
    private String label;
    private String cep;
    private boolean deactivate;

    public Address(String state, String country, String street, int number, String complement, String label, String cep, boolean deactivate) {
        this.state = state;
        this.country = country;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.label = label;
        this.cep = cep;
        this.deactivate = deactivate;
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

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isDeactivate() {
        return deactivate;
    }

    public void setDeactivate(boolean deactivate) {
        this.deactivate = deactivate;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", street='" + street + '\'' +
                ", number=" + number +
                ", complement='" + complement + '\'' +
                ", label='" + label + '\'' +
                ", cep='" + cep + '\'' +
                ", deactivate=" + deactivate + '\'' +
                '}';
    }
}
