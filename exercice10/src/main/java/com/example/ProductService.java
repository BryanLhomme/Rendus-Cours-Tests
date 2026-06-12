package com.example;

import java.util.List;

public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<Product> search(String keyword) {
        return productRepository.findByKeyword(keyword);
    }

    public List<Product> searchByMaxPrice(double maxPrice) {
        return productRepository.findByMaxPrice(maxPrice);
    }
}
