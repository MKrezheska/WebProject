package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final JpaProductRepository repository;

    public ProductRepositoryImpl(JpaProductRepository repository) {
        this.repository = repository;
    }


    @Override
    public Product save(Product product) {
        try {
            return this.repository.saveAndFlush(product);
        } catch (DataIntegrityViolationException e) {
            System.out.println("Duplicate found, skipping");
        }
//
        return product;

//        return this.repository.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return this.repository.findAll();
    }

    @Override
    public Optional<Product> findById(Long productId) {
        return this.repository.findById(productId);
    }

    @Override
    public Optional<Product> findByName(String name) {
        return this.repository.findByName(name);
    }

    @Override
    public void deleteById(Long productId) {
        this.repository.deleteById(productId);
    }

    @Override
    public List<Product> findByUrlContains(String store) {
        return this.repository.findByUrlContains(store);
    }
}
