package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface JpaProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByName(String name);
    List<Product> findByUrlContains(String store);
}
