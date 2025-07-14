package com.example.headphones_ecommerce_store.model;

import java.util.List;
import java.util.ArrayList;

public class Order {
    private String id;
    private String userId;
    private List<OrderItem> items;
    private Address shippingAddress;
    private long orderDate; // Timestamp
    private double totalPrice;
    private String status;
    private PaymentDetails paymentDetails;

    public Order() {
        this.items = new ArrayList<>();
    }

    public Order(String id, String userId, List<OrderItem> items, Address shippingAddress,
                 long orderDate, double totalPrice, String status, PaymentDetails paymentDetails) {
        this.id = id;
        this.userId = userId;
        this.items = items != null ? items : new ArrayList<>();
        this.shippingAddress = shippingAddress;
        this.orderDate = orderDate;
        this.totalPrice = totalPrice;
        this.status = status;
        this.paymentDetails = paymentDetails;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public List<OrderItem> getItems() { return items; }
    public void setItems(List<OrderItem> items) { this.items = items; }
    public Address getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(Address shippingAddress) { this.shippingAddress = shippingAddress; }
    public long getOrderDate() { return orderDate; }
    public void setOrderDate(long orderDate) { this.orderDate = orderDate; }
    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public PaymentDetails getPaymentDetails() { return paymentDetails; }
    public void setPaymentDetails(PaymentDetails paymentDetails) { this.paymentDetails = paymentDetails; }
}

