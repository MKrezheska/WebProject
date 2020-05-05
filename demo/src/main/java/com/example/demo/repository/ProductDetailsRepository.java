package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;

import java.util.Optional;

public interface ProductDetailsRepository {
    ProductDetails save(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution, Product product);

    Optional<ProductDetails> findById(Long productId);

    void deleteById(Long productId);
}
