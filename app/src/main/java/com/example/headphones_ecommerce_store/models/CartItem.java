package com.example.headphones_ecommerce_store.models;

public class CartItem {
    private long productId;
    private String name;
    private double price;
    private String imageUrl;
    private int quantity;
    private boolean isSelected; // Thêm thuộc tính để quản lý trạng thái chọn

    public CartItem(long productId, String name, double price, String imageUrl, int quantity) {
        this.productId = productId;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.quantity = quantity;
        this.isSelected = true; // Mặc định là đã chọn khi thêm vào giỏ
    }

    // Getters and Setters
    public long getProductId() { return productId; }
    public void setProductId(long productId) { this.productId = productId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}
