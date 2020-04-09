package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

//import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;
import com.gargoylesoftware.htmlunit.javascript.*;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<WorkerNep> workers;

    public WorkerNep w1;
    public WorkerNep w2;
    public WorkerNep w3;

    @Override
    public void loadContents(String[] urls) {

        workers = new ArrayList<>(urls.length);
        for (String url : urls) {
            w1 = new WorkerNep(url+"1");
            w2 = new WorkerNep(url+"2");
            if(url.contains("setec")){
                w3 = new WorkerNep(url+"3");
                workers.add(w3);
                new Thread(w3).start();
            }
            workers.add(w1);
            workers.add(w2);
            new Thread(w1).start();
            new Thread(w2).start();
        }
        ArrayList<String> productRepo=new ArrayList<>(500);
        ArrayList<String> imgRepo=new ArrayList<>(500);
        ArrayList<String> nameRepo=new ArrayList<>(500);

        for (WorkerNep w : workers) {
            ArrayList<String> results=w.waitForResults();
            if (results != null) {
                productRepo.addAll(w.getProductUrls());
                imgRepo.addAll(w.getProductImgs());
                nameRepo.addAll(w.getProductNames());

            }
            else
                System.err.println(w.getName()+" had some error!");
        }

        for (int i = 0; i < productRepo.size(); i++) {
            createProduct(nameRepo.get(i),
                    productRepo.get(i),
                    imgRepo.get(i));
        }


    }

    @Override
    public List<Product> listProducts() throws IOException {
        String[] urls = new String[]{
                "http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100&page=",
                "https://www.neptun.mk/pocetna/categories/KOMPJUTERI/prenosni_kompjuteri.nspx?items=100&page="};
        loadContents(urls);

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

