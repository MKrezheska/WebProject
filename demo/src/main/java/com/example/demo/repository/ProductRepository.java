package com.example.demo.repository;


import com.example.demo.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);

    List<Product> getAllProducts();

    Optional<Product> findById(Long productId);

    Optional<Product> findByName(String name);

    void deleteById(Long productId);

    List<Product> findByUrlContains(String store);
}
