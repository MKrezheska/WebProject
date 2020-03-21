package com.example.demo.service.impl;

import com.example.demo.repository.ProductRepository;
import com.example.demo.model.Product;
import com.example.demo.service.ProductService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void loadContents(String url) throws IOException {

        Document document = Jsoup.connect(url).get();

        String nameAndUrlSelector = "#mora_da_ima_prazno_mesto > a";
        String imgSelector = "#mfilter-content-container > div.product-grid.active > div > div > div > div.left > div.image > a > img";
        Elements productElements = document.select(nameAndUrlSelector);
        Elements imgElements = document.select(imgSelector);
        ArrayList<String> productNames;
        ArrayList<String> productUrls;
        ArrayList<String> imgUrls;

        productNames = productElements.stream()
                .map(Element::text)
                .collect(Collectors
                        .toCollection(ArrayList::new));
        productUrls = productElements.stream()
                .map(e -> e.attributes().getIgnoreCase("href"))
                .collect(Collectors.toCollection(ArrayList::new));
        imgUrls = imgElements.stream()
                .map(e -> e.attributes().getIgnoreCase("data-echo"))
                .collect(Collectors.toCollection(ArrayList::new));

        for (int i = 0; i < 100; i++) {
            createProduct(productNames.get(i),
                    productUrls.get(i),
                    imgUrls.get(i));
        }


    }

    @Override
    public List<Product> listProducts() throws IOException {
        loadContents("http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100");
        return this.productRepository.getAllProducts();
    }

    @Override
    public Product createProduct(String name, String url, String imgUrl) {
        Product product = new Product(Math.abs(new Random().nextLong()), name, url, imgUrl);
        return this.productRepository.save(product);
    }

    @Override
    public Product getProduct(Long productId) {
        /* TODO
         * Make an Exception!
         */
        return this.productRepository.findById(productId).orElse(null);
    }

    @Override
    public Product updateProduct(Long productId, String name, String url, String imgUrl) {
        Product product = this.productRepository.findById(productId).orElse(null);
        product.setName(name);
        product.setUrl(url);
        product.setImgUrl(imgUrl);
        return this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        this.productRepository.deleteById(productId);

    }
}
