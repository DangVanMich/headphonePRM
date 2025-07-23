package com.example.headphones_ecommerce_store.models;

import java.io.Serializable;

public class RankingItem implements Serializable {
    private long id;
    private final String name;
    private final String imageUrl;
    private final double price;

    public RankingItem(long id, String name, String imageUrl, double price) {
        this.id = id;
        this.name = name;
        this.imageUrl = imageUrl;
        this.price = price;
    }


    public long getId() { return id; }
    public String getName() { return name; }
    public String getImageUrl() { return imageUrl; }
    public double getPrice() { return price; }
}
