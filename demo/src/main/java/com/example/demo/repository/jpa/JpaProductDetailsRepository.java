package com.example.demo.repository.jpa;

import com.example.demo.model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JpaProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
}
