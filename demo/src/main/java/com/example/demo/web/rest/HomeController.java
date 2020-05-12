package com.example.demo.web.rest;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;
import com.example.demo.model.vm.Page;
import com.example.demo.service.ProductService;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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

    @GetMapping("/filter")
    public List<Product> searchProducts(@RequestParam(defaultValue = "", required = false) String display,
                                               @RequestParam(defaultValue = "", required = false) String graphicsCard,
                                               @RequestParam(defaultValue = "", required = false) String internalMemory,
                                               @RequestParam(defaultValue = "", required = false) String memory,
                                               @RequestParam(defaultValue = "", required = false) String processor,
                                               @RequestParam(defaultValue = "", required = false) String resolution) {
        List<ProductDetails> details = productService.searchProducts(display, graphicsCard, internalMemory, memory, processor, resolution);
        return details.stream().map(ProductDetails::getProduct).collect(Collectors.toList());


        // /products?display=15.6&graphicsCard=AMD&internalMemory=8&memory=256&processor=AMD&resolution=1920
    }
}



