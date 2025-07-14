package com.example.headphones_ecommerce_store.model;

// package com.example.headphones_ecommerce_store.model; // Original package

public class Address { // Assuming you want a Java version of the provided Address.kt
    private String id; // Unique address ID
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private boolean isDefault; // Changed from isDefault to isDefault for Java bean convention

    public Address() {
    }

    public Address(String id, String street, String city, String state, String zipCode, String country, boolean isDefault) {
        this.id = id;
        this.street = street;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.country = country;
        this.isDefault = isDefault;
    }

    // Getters
    public String getId() { return id; }
    public String getStreet() { return street; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getZipCode() { return zipCode; }
    public String getCountry() { return country; }
    public boolean isDefault() { return isDefault; } // Getter for boolean

    // Setters
    public void setId(String id) { this.id = id; }
    public void setStreet(String street) { this.street = street; }
    public void setCity(String city) { this.city = city; }
    public void setState(String state) { this.state = state; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    public void setCountry(String country) { this.country = country; }
    public void setDefault(boolean isDefault) { this.isDefault = isDefault; }
}

