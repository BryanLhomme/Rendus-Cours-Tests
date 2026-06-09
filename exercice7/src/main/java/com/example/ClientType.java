package com.example;

public enum ClientType {
    STANDARD(0), PREMIUM(10), VIP(20);

    private final int discountRate;

    ClientType(int discountRate) {
        this.discountRate = discountRate;
    }

    public int getDiscountRate() { return discountRate; }
}
