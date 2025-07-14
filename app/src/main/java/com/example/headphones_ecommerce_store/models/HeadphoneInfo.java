package com.example.headphones_ecommerce_store.models;

public class HeadphoneInfo {
    private String name;
    private String brand;
    private double price;
    private String imageUrl;

    public HeadphoneInfo(String name, String brand, double price, String imageUrl) {
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
    }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}
