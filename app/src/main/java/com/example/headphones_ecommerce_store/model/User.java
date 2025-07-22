package com.example.headphones_ecommerce_store.model;

import java.util.List;
import java.util.ArrayList; // For initializing lists

public class User {
    private String id;
    private String email;
    private String displayName;
    private String profileImageUrl;
    private List<Address> shippingAddresses;
    private List<PaymentMethod> paymentMethods;
    private List<String> orderHistory; // List of Order IDs
    private String password;


    public User() {
        this.shippingAddresses = new ArrayList<>(); // Initialize lists
        this.paymentMethods = new ArrayList<>();
        this.orderHistory = new ArrayList<>();
    }

    public User(String id, String email, String displayName, String profileImageUrl,
                List<Address> shippingAddresses, List<PaymentMethod> paymentMethods, List<String> orderHistory) {
        this.id = id;
        this.email = email;
        this.displayName = displayName;
        this.profileImageUrl = profileImageUrl;
        this.shippingAddresses = shippingAddresses != null ? shippingAddresses : new ArrayList<>();
        this.paymentMethods = paymentMethods != null ? paymentMethods : new ArrayList<>();
        this.orderHistory = orderHistory != null ? orderHistory : new ArrayList<>();
    }

    // Getters
    public String getId() { return id; }
    public String getEmail() { return email; }
    public String getDisplayName() { return displayName; }
    public String getProfileImageUrl() { return profileImageUrl; }
    public List<Address> getShippingAddresses() { return shippingAddresses; }
    public List<PaymentMethod> getPaymentMethods() { return paymentMethods; }
    public List<String> getOrderHistory() { return orderHistory; }

    // Setters
    public void setId(String id) { this.id = id; }
    public void setEmail(String email) { this.email = email; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    public void setProfileImageUrl(String profileImageUrl) { this.profileImageUrl = profileImageUrl; }
    public void setShippingAddresses(List<Address> shippingAddresses) { this.shippingAddresses = shippingAddresses; }
    public void setPaymentMethods(List<PaymentMethod> paymentMethods) { this.paymentMethods = paymentMethods; }
    public void setOrderHistory(List<String> orderHistory) { this.orderHistory = orderHistory; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // In User.java
    public String getRegistrationDate() {
        return null; // Implement getter if registration_date is added as a field
    }
}
