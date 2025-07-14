package com.example.headphones_ecommerce_store.model;

public class OrderItem {
    private String productId;
    private int quantity;
    private double priceAtPurchase;
    private String productName;
    private String productImageUrl;

    public OrderItem() {
    }

    public OrderItem(String productId, int quantity, double priceAtPurchase, String productName, String productImageUrl) {
        this.productId = productId;
        this.quantity = quantity;
        this.priceAtPurchase = priceAtPurchase;
        this.productName = productName;
        this.productImageUrl = productImageUrl;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public double getPriceAtPurchase() { return priceAtPurchase; }
    public void setPriceAtPurchase(double priceAtPurchase) { this.priceAtPurchase = priceAtPurchase; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
}

