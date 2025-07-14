package com.example.headphones_ecommerce_store.controller;

public class Order {
    public String orderId;
    public String date;
    public String total;
    public String status;
    public String products;

    public Order(String orderId, String date, String total, String status, String products) {
        this.orderId = orderId;
        this.date = date;
        this.total = total;
        this.status = status;
        this.products = products;
    }
}

