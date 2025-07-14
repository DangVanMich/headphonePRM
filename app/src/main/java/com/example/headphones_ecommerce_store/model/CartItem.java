package com.example.headphones_ecommerce_store.model;

public class CartItem {
    private String productId;
    private int quantity;
    private String productName;
    private double productPrice;
    private String productImageUrl;

    public CartItem() {
    }

    public CartItem(String productId, int quantity, String productName, double productPrice, String productImageUrl) {
        this.productId = productId;
        this.quantity = quantity;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImageUrl = productImageUrl;
    }

    // Getters and Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public String getProductName() { return productName; }
    public void setProductName(String productName) { this.productName = productName; }
    public double getProductPrice() { return productPrice; }
    public void setProductPrice(double productPrice) { this.productPrice = productPrice; }
    public String getProductImageUrl() { return productImageUrl; }
    public void setProductImageUrl(String productImageUrl) { this.productImageUrl = productImageUrl; }
}
