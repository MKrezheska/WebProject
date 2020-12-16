package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    void loadContents() throws IOException;
    List<Product> listProducts() throws IOException;
    Product createProduct(String name, String url, String imgUrl,String description,String price, String clubPrice,String pricePartial, String fullDescription);
    Product getProduct(Long productId);
    Product updateProduct(Long productId, String name, String url, String imgUrl,String description,String price, String clubPrice,String pricePartial);
    void deleteProduct(Long productId);
    List<ProductDetails> searchProducts(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution);
    List<Product> findByUrlContains(String store);
    Product updateSimilarProduct(Long id, Product similar);
}