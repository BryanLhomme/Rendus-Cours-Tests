package com.example;

public class Order {
    private final String clientEmail;
    private final String productReference;
    private final int quantity;

    public Order(String clientEmail, String productReference, int quantity) {
        this.clientEmail = clientEmail;
        this.productReference = productReference;
        this.quantity = quantity;
    }

    public String getClientEmail() { return clientEmail; }
    public String getProductReference() { return productReference; }
    public int getQuantity() { return quantity; }
}
