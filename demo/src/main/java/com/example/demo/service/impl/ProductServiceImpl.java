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
    public void loadContents(String[] urls) throws IOException {

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


//
//@Data
//class WorkerNep implements Runnable {
//
//    private String url;
//    private Elements results;
//    private ArrayList<String> productUrls;
//    private ArrayList<String> productImgs;
//    private ArrayList<String> productNames;
//    private String name;
//    private static int number = 0;
//
//    private final Object lock = new Object();
//
//    public WorkerNep(String url) {
//        this.url = url;
//        this.name = "Worker-" + (number++);
//        productUrls=new ArrayList<>();
//        productImgs=new ArrayList<>();
//        productNames=new ArrayList<>();
//    }
//
//
//    @Override
//    public void run() {
//        try {
//            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
//            java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);
//
//            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_68);
//            webClient.getOptions().setJavaScriptEnabled(true); // MORA ZA NEPTUN
//            //NE BRISHI ZNAM DEKA VADI WARNING AMA MORA ZA NEPTUN
//            webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
//            webClient.getOptions().setCssEnabled(false); // ZA POBRZO DEMEK
//            webClient.getOptions().setThrowExceptionOnScriptError(false);
//            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
//            webClient.getOptions().setTimeout(10 * 1000); // ZA POBRZO DEMEK
//            webClient.setAjaxController(new NicelyResynchronizingAjaxController()); //PROKLET NEPTUN
//
//
//            HtmlPage page = webClient.getPage(url);
//            //NE SUM SIGURNA DALI OVA TREBA
//            //AMA MISLAM DEKA TREBA ZA DA SE VCHITAAT SITE PRODUKTI
////            webClient.waitForBackgroundJavaScriptStartingBefore(200);
//            // CHEKAJ 100 SEKUNDI
//            //VKUPNO SE 389 PRODUKTI (AKO NE TI SE VCHITAT SITE ODKOMENTIRAJ GO TO POGORE ILI ZGOLEMI GI SEKUNDITE)
//            //KE TI IZLEGVAT WARNINGS POVRZANI SO HTMLUNIT
//            //NE SE SEKIRAJ ZARADI SKRIPTITE NA NEPTUN E (mislam...)
//            webClient.waitForBackgroundJavaScript(100 * 1000);
//
//            String pageAsXml = page.asXml();
//
//            Document doc = Jsoup.parse(pageAsXml);
//
//
//            Elements links = new Elements(1000);
//            Elements img = new Elements(1000);
//            ArrayList<String> name = new ArrayList<>(1000);
//            Elements all;
//            if(url.contains("http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100&page=")) {
//                all = doc.getElementsByClass("product");
//                for (Element el : all) {
//                    Element e = el.getElementsByTag("a").first();
//                    links.add(e);
//                    e = el.getElementsByTag("img").first();
//                    img.add(e);
//                    e = el.getElementsByClass("name").first();
//                    name.add(e.getElementsByTag("a").text());
//                }
//            }
//            else {
//                all = doc.select("div[class*=product-list-item-grid]");
//                for (Element el : all) {
//                    Element e = el.getElementsByTag("a").first();
//                    links.add(e);
//                    e = el.getElementsByTag("img").first();
//                    img.add(e);
//                    name.add(el.getElementsByTag("h2").text());
//
//                }
//            }
//
//
//
//            synchronized (lock) {
//                int i=0;
//                for (Element link : links) {
//                    if (link.absUrl("href").contains("setec")) {
//                        if (link.absUrl("href").contains("product_id")) {
//                            productUrls.add(link.absUrl("href"));
//                            productImgs.add(img.get(i).absUrl("data-echo"));
//                            productNames.add(name.get(i));
//                            i++;
//                        }
//
//                    }
//                    if (link.hasAttr("ng-href")==true) {
//                        productUrls.add("https://www.neptun.mk/"+link.attr("ng-href"));
//                        productImgs.add("https://www.neptun.mk/"+img.get(i).attr("ng-src"));
//                        productNames.add(name.get(i));
//                        i++;
//                    }
//                }
//                this.results = links;
//                lock.notifyAll();
//            }
//        } catch (IOException e) {
//            System.err.println("Error while parsing: "+this.url);
//            e.printStackTrace();
//        }
//    }
//
//    public ArrayList<String> waitForResults() {
//        synchronized (lock) {
//            try {
//                while (this.results == null) {
//                    lock.wait();
//                }
//                return this.productUrls;
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            return null;
//
//
//        }
//    }
//}
