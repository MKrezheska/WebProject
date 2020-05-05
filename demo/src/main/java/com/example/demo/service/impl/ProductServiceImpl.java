package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.model.exceptions.InvalidProductIdException;
import com.example.demo.repository.ProductDetailsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

//import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailsRepository productDetailsRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductDetailsRepository productDetailsRepository) {
        this.productRepository = productRepository;
        this.productDetailsRepository = productDetailsRepository;
    }

    public List<WorkerNep> workers;

    public WorkerNep w1;
    public WorkerNep w2;
    public WorkerNep w3;

    @PostConstruct
    @Override
    public void loadContents() {

        String[] urls = new String[]{
                "http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100&page=",
                "https://www.neptun.mk/pocetna/categories/KOMPJUTERI/prenosni_kompjuteri.nspx?items=100&page="};

        workers = new ArrayList<>(urls.length);
        for (String url : urls) {
            w1 = new WorkerNep(url + "1");

            if (url.contains("setec")) {
                w2 = new WorkerNep(url + "2");
                w3 = new WorkerNep(url + "3");
                workers.add(w2);
                workers.add(w3);
                new Thread(w2).start();
                new Thread(w3).start();
            }
            workers.add(w1);

            new Thread(w1).start();

        }
        ArrayList<String> productRepo = new ArrayList<>(500);
        ArrayList<String> imgRepo = new ArrayList<>(500);
        ArrayList<String> nameRepo = new ArrayList<>(500);
        ArrayList<String> priceRepo = new ArrayList<>(500);
        ArrayList<String> clubPriceRepo = new ArrayList<>(500);
        ArrayList<String> pricePartialRepo = new ArrayList<>(500);

        for (WorkerNep w : workers) {
            ArrayList<String> results = w.waitForResults();
            if (results != null) {
                productRepo.addAll(w.getProductUrls());
                imgRepo.addAll(w.getProductImgs());
                nameRepo.addAll(w.getProductNames());
                priceRepo.addAll(w.getProductPrices());
                clubPriceRepo.addAll(w.getProductClubPrices());
                pricePartialRepo.addAll(w.getProductPricePartial());

            } else
                System.err.println(w.getName() + " had some error!");
        }


        List<WorkerDesc> workers2 = new ArrayList<>(productRepo.size());
        for (String url : productRepo) {
            WorkerDesc w = new WorkerDesc(url);
            workers2.add(w);
            new Thread(w).start();
        }

        ArrayList<String> productDesc = new ArrayList<>(500);

        // Retrieve results
        for (WorkerDesc w : workers2) {
            ArrayList<String> prodd = w.waitForResults();
            if (prodd != null) {
//                System.out.println(w.getName() + ": ");
//                System.out.println(prodd);
//                for (String s : prodd) {
//                    System.out.println(s);
//                }
                productDesc.addAll(prodd);
            } else
                System.err.println(w.getName() + " had some error!");
        }


        for (int i = 0; i < productRepo.size(); i++) {
            Product product = createProduct(nameRepo.get(i),
                    productRepo.get(i),
                    imgRepo.get(i),
                    productDesc.get(i),
                    priceRepo.get(i),
                    clubPriceRepo.get(i),
                    pricePartialRepo.get(i));
            // ovde da ja povikuvame funkcijata so ke vrakjat lista od properties za dadeno description vo properties lista
            // productDetailsRepository.save(properties.get(0), properties.get(1), properties.get(2), properties.get(3), properties.get(4), properties.get(5), product);

            // za proba vo baza
            // productDetailsRepository.save(getDisplay(product.getDescription()), "Nvidia", "1 TB SSD", "4 GB RAM", "Intel Core i5", "200x1500", product);

        }


    }

//    private static String getResolution(String description) {
//        String[] properties = description.split("\\|");
//        String toReturn = "Not found";
//        for (String property : properties) {
//            if (property.matches(".*[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}.*")) {
//
//                property = property.toLowerCase().replaceAll("[^[0-9]x\".,\\s]", "").trim();
//                String[] parts = property.split("\\s+");
//                for (int i = 0; i < parts.length; i++) {
//                    if (parts[i].length() > 1 && parts[i].contains("x"))
//                        toReturn = parts[i];
//                    else if (parts[i].equals("x") && i > 0 && i < parts.length - 1)
//                        toReturn = String.join("", parts[i - 1], parts[i], parts[i + 1]);
//                }
//
//            } else if (property.matches("\\s+[0-9]{3,4}-[0-9]{3,4}\\s+")) {
//                toReturn = property.replace("-", "x").trim();
//            }
//
//        }
//        return toReturn;
//    }

//    public static String getDisplay(String description) {
//        String[] properties = description.split("\\|");
//        String toReturn = "Not found";
//        for (String property : properties) {
//            if (property.matches(".*[12][13567][.,][13689]?\"?.*|.*1[456][.,]?0?\".*|.*14[,.][01].*") && toReturn.equals("Not found")) {
//                toReturn = property.replaceAll("[^0-9\".,]", "");
//                if(toReturn.length() > 4 && !toReturn.contains("\"") )
//                    toReturn = toReturn.substring(0, 4);
//                if(toReturn.contains("\"") && (toReturn.indexOf("\"")==2 || toReturn.indexOf("\"")==3)){
//                    toReturn = toReturn.split("\"")[0];
//                }
//                if(toReturn.contains("\"") && toReturn.indexOf("\"")>3){
//                    toReturn = toReturn.substring(toReturn.indexOf("\"")-4, toReturn.indexOf("\""));
//                }
//                if(!toReturn.contains(".") && !toReturn.contains(",") && toReturn.length() > 2)
//                    toReturn = "Not found";
//
//            }
//        }
//        return toReturn;
//    }

    @Override
    public List<Product> listProducts() {
        return this.productRepository.getAllProducts();
    }

    @Override
    public Product createProduct(String name, String url, String imgUrl, String description, String price, String clubPrice, String pricePartial) {
        Product product = new Product(name, url, imgUrl, description, price, clubPrice, pricePartial);
        return this.productRepository.save(product);
    }

    @Override
    public Product getProduct(Long productId) {
        return this.productRepository.findById(productId).orElseThrow(InvalidProductIdException::new);
    }

    @Override
    public Product updateProduct(Long productId, String name, String url, String imgUrl, String description, String price, String clubPrice, String pricePartial) {
        Product product = this.productRepository.findById(productId).orElseThrow(InvalidProductIdException::new);
        product.setName(name);
        product.setUrl(url);
        product.setImgUrl(imgUrl);
        product.setDescription(description);
        product.setPrice(price);
        product.setClubPrice(clubPrice);
        product.setPricePartial(pricePartial);
        return this.productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long productId) {
        this.productRepository.deleteById(productId);

    }
}