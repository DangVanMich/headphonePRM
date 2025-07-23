package com.example.headphones_ecommerce_store.model;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
// import android.os.Parcel; // For Parcelable
// import android.os.Parcelable; // For Parcelable

public class Product implements Serializable {
    private long id;
    private String name;
    private String brand;
    private String description;
    private double price;
    private List<String> imageUrls;

    private String thumbnailImageUrl;

    private String category;
    private List<String> features;
    private List<String> colorOptions;
    private int stockQuantity;
    private float averageRating;
    private int reviewCount;
    private Map<String, String> specifications;

    // Constructors
    public Product() {
        // Default constructor for libraries like Firebase Firestore or Gson
    }

    public String getThumbnailImageUrl() {
        return thumbnailImageUrl;
    }

    public void setThumbnailImageUrl(String thumbnailImageUrl) {
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    public Product(long id, String name, String brand, String description, double price,
                   List<String> imageUrls, String category, List<String> features,
                   List<String> colorOptions, int stockQuantity, float averageRating,
                   int reviewCount, Map<String, String> specifications) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.imageUrls = imageUrls;
        this.category = category;
        this.features = features;
        this.colorOptions = colorOptions;
        this.stockQuantity = stockQuantity;
        this.averageRating = averageRating;
        this.reviewCount = reviewCount;
        this.specifications = specifications;
    }

    public Product(long id, String name, String description, double price, String thumbnailImageUrl) {
        this.id = id;
        this.name = name;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.description = description;
        this.price = price;
    }

    public Product(long id, String name, String brand, String description, double price, String thumbnailImageUrl, float averageRating){
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.thumbnailImageUrl = thumbnailImageUrl;
        this.averageRating = averageRating;
    }

    public Product(long id, String name, String brand, String description, double price, String thumbnailImageUrl) {
        this.id = id;
        this.name = name;
        this.brand = brand;
        this.description = description;
        this.price = price;
        this.thumbnailImageUrl = thumbnailImageUrl;
    }

    // Getters
    public long getId() { return id; }

    public String getName() { return name; }
    public String getBrand() { return brand; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public List<String> getImageUrls() { return imageUrls; }
    public String getCategory() { return category; }
    public List<String> getFeatures() { return features; }
    public List<String> getColorOptions() { return colorOptions; }
    public int getStockQuantity() { return stockQuantity; }
    public float getAverageRating() { return averageRating; }
    public int getReviewCount() { return reviewCount; }
    public Map<String, String> getSpecifications() { return specifications; }

    // Setters
    public void setId(long id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBrand(String brand) { this.brand = brand; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }
    public void setCategory(String category) { this.category = category; }
    public void setFeatures(List<String> features) { this.features = features; }
    public void setColorOptions(List<String> colorOptions) { this.colorOptions = colorOptions; }
    public void setStockQuantity(int stockQuantity) { this.stockQuantity = stockQuantity; }
    public void setAverageRating(float averageRating) { this.averageRating = averageRating; }
    public void setReviewCount(int reviewCount) { this.reviewCount = reviewCount; }
    public void setSpecifications(Map<String, String> specifications) { this.specifications = specifications; }

    // toString(), equals(), hashCode() methods (optional but good practice)
    // ...

    // Parcelable implementation (if you uncomment the interface)
    // protected Product(Parcel in) { ... read from parcel ... }
    // public static final Creator<Product> CREATOR = new Creator<Product>() { ... };
    // @Override public int describeContents() { return 0; }
    // @Override public void writeToParcel(Parcel dest, int flags) { ... write to parcel ... }
}

