package com.example;

import java.util.List;

public interface ProductRepository {
    List<Product> findByKeyword(String keyword);
    List<Product> findByMaxPrice(double maxPrice);
}
