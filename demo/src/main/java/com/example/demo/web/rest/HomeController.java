package com.example.demo.web.rest;

import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/products", produces = MimeTypeUtils.APPLICATION_JSON_VALUE)
public class HomeController {

    private final ProductService productService;

    public HomeController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<Product> getAllProducts() throws IOException {
        return productService.listProducts();
    }
}
