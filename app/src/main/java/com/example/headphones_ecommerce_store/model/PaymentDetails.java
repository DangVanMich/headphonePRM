package com.example.headphones_ecommerce_store.model;

public class PaymentDetails {
    private String transactionId;
    private String paymentMethodUsed;
    private double amountPaid;
    private long paymentDate;

    public PaymentDetails() {
    }

    public PaymentDetails(String transactionId, String paymentMethodUsed, double amountPaid, long paymentDate) {
        this.transactionId = transactionId;
        this.paymentMethodUsed = paymentMethodUsed;
        this.amountPaid = amountPaid;
        this.paymentDate = paymentDate;
    }

    // Getters and Setters
    public String getTransactionId() { return transactionId; }
    public void setTransactionId(String transactionId) { this.transactionId = transactionId; }
    public String getPaymentMethodUsed() { return paymentMethodUsed; }
    public void setPaymentMethodUsed(String paymentMethodUsed) { this.paymentMethodUsed = paymentMethodUsed; }
    public double getAmountPaid() { return amountPaid; }
    public void setAmountPaid(double amountPaid) { this.amountPaid = amountPaid; }
    public long getPaymentDate() { return paymentDate; }
    public void setPaymentDate(long paymentDate) { this.paymentDate = paymentDate; }
}

