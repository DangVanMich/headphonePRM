package com.example.headphones_ecommerce_store.model;

public class PaymentMethod {
    private String id;
    private String type;
    private String lastFourDigits;
    private String cardType;

    public PaymentMethod() {
    }

    public PaymentMethod(String id, String type, String lastFourDigits, String cardType) {
        this.id = id;
        this.type = type;
        this.lastFourDigits = lastFourDigits;
        this.cardType = cardType;
    }

    // Getters and Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getLastFourDigits() { return lastFourDigits; }
    public void setLastFourDigits(String lastFourDigits) { this.lastFourDigits = lastFourDigits; }
    public String getCardType() { return cardType; }
    public void setCardType(String cardType) { this.cardType = cardType; }
}

