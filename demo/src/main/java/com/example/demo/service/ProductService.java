package com.example.demo.service;

import com.example.demo.model.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    void loadContents() throws IOException;
    List<Product> listProducts() throws IOException;
    Product createProduct(String name, String url, String imgUrl);
    Product getProduct(Long productId);
    Product updateProduct(Long productId, String name, String url, String imgUrl);
    void deleteProduct(Long productId);


}