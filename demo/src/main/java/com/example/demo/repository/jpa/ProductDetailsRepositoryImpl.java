package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;
import com.example.demo.repository.ProductDetailsRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class ProductDetailsRepositoryImpl implements ProductDetailsRepository {

    private final JpaProductDetailsRepository repository;

    public ProductDetailsRepositoryImpl(JpaProductDetailsRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductDetails save(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution, Product product) {
        ProductDetails details = new ProductDetails(display, graphicsCard, internalMemory, memory, processor, resolution, product);
        return this.repository.save(details);
    }

    @Override
    public Optional<ProductDetails> findById(Long productId) {
        return this.repository.findById(productId);
    }

    @Override
    public void deleteById(Long productId) {
        this.repository.deleteById(productId);
    }
}
