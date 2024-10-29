package com.example.khiata.models;

public class Historic {
    private int id;
    private int cart_id;
    private double FinalValue;
    private String userCpf;
    private String paymentmethod;
    private String status;
    private String created_Date;
    private String orderDate;
    private String deliveryDate;

    public Historic(int id, int cart_id, double finalValue, String userCpf, String paymentmethod, String status, String created_Date, String orderDate, String deliveryDate) {
        this.id = id;
        this.cart_id = cart_id;
        FinalValue = finalValue;
        this.userCpf = userCpf;
        this.paymentmethod = paymentmethod;
        this.status = status;
        this.created_Date = created_Date;
        this.orderDate = orderDate;
        this.deliveryDate = deliveryDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCart_id() {
        return cart_id;
    }

    public void setCart_id(int cart_id) {
        this.cart_id = cart_id;
    }

    public double getFinalValue() {
        return FinalValue;
    }

    public void setFinalValue(double finalValue) {
        FinalValue = finalValue;
    }

    public String getUserCpf() {
        return userCpf;
    }

    public void setUserCpf(String userCpf) {
        this.userCpf = userCpf;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreated_Date() {
        return created_Date;
    }

    public void setCreated_Date(String created_Date) {
        this.created_Date = created_Date;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    @Override
    public String toString() {
        return "Historic{" +
                "id=" + id +
                ", cart_id=" + cart_id +
                ", FinalValue=" + FinalValue +
                ", userCpf='" + userCpf + '\'' +
                ", paymentmethod='" + paymentmethod + '\'' +
                ", status='" + status + '\'' +
                ", created_Date='" + created_Date + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                '}';
    }
}
