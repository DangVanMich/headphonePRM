package com.example.headphones_ecommerce_store.models;
import java.io.Serializable;
//Không dùng nữa
public class HeadphoneInfo implements Serializable {
    private long id;
    private String name;
    private String brand;
    private double price;
    private String imageUrl;

    public HeadphoneInfo(long id, String name, String brand, double price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    public long getId() { return id; }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public double getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
}