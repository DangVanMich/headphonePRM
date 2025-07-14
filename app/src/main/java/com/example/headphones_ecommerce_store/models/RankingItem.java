package com.example.headphones_ecommerce_store.models;

public class RankingItem {
    private final String name;
    private final String imageUrl;
    private final double price;

    public RankingItem(String name, String imageUrl, double price) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }

    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
}
