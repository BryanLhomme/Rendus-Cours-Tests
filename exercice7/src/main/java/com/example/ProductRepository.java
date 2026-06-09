package com.example;

public interface ProductRepository {
    boolean exists(String reference);
    Product findByReference(String reference);
}
