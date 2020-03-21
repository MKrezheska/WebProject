package com.example.demo.repository;


import com.example.demo.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    List<Product> getAllProducts();

    Optional<Product> findById(Long productId);

    void deleteById(Long productId);
}
