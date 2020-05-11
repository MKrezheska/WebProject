package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;

import java.util.List;
import java.util.Optional;

public interface ProductDetailsRepository {
    ProductDetails save(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution, Product product);

    Optional<ProductDetails> findById(Long productId);

    void deleteById(Long productId);

    List<ProductDetails> searchProducts(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution);

}
