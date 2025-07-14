package com.example.headphones_ecommerce_store.model;

public class Review {
    private String id;
    private String productId;
    private String userId;
    private String userName;
    private float rating;
    private String comment;
    private long reviewDate;

    public Review() {
    }

    public Review(String id, String productId, String userId, String userName, float rating, String comment, long reviewDate) {
        this.id = id;
        this.productId = productId;
        this.userId = userId;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.reviewDate = reviewDate;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }
    public float getRating() { return rating; }
    public void setRating(float rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public long getReviewDate() { return reviewDate; }
    public void setReviewDate(long reviewDate) { this.reviewDate = reviewDate; }
}


