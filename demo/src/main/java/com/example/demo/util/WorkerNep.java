package com.example.demo.util;

import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.presenceOfAllElementsLocatedBy;


@SuppressWarnings("Duplicates")
@Data
public class WorkerNep implements Runnable {
    private String url;
    private Elements results;
    private ArrayList<String> productUrls;
    private ArrayList<String> productImgs;
    private ArrayList<String> productNames;
    private ArrayList<String> productPrices;
    private ArrayList<String> productClubPrices;
    private ArrayList<String> productPricePartial;
    private String name;
    private static int number = 0;

    private final Object lock = new Object();

    public WorkerNep(String url) {
        this.url = url;
        this.name = "Worker-" + (number++);
        productUrls=new ArrayList<>();
        productImgs=new ArrayList<>();
        productNames=new ArrayList<>();
        productPrices=new ArrayList<>();
        productClubPrices=new ArrayList<>();
        productPricePartial=new ArrayList<>();
    }
    public ArrayList<String> getProductUrls() {
        return productUrls;
    }
    public ArrayList<String> getProductPrices() {
        return productPrices;
    }
    public ArrayList<String> getProductClubPrices() {
        return productClubPrices;
    }
    public ArrayList<String> getProductPricePartial() {
        return productPricePartial;
    }

    public ArrayList<String> getProductImgs() {
        return productImgs;
    }
    public ArrayList<String> getProductNames() {
        return productNames;
    }
    public String getName() {
        return name;
    }
    private static void print(String msg, Object... args) {
        System.out.println(String.format(msg, args));
    }

    private static String trim(String s, int width) {
        if (s.length() > width)
            return s.substring(0, width-1) + ".";
        else
            return s;
    }
    @Override
    public void run() {
        try {

            Document doc = null;
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

            if (this.url.contains("neptun")) {
//                String chromeDriverPath = "C:\\Users\\user\\Documents\\chromedriver_win32\\chromedriver.exe" ;
                String chromeDriverPath = "C:\\Users\\user\\Downloads\\chromedriver_win32\\chromedriver.exe" ;
                System.setProperty("webdriver.chrome.driver", chromeDriverPath);
                ChromeOptions options = new ChromeOptions(); options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200","--ignore-certificate-errors");
                WebDriver driverCh = new ChromeDriver(options);
                driverCh.get("https://www.neptun.mk/pocetna/categories/KOMPJUTERI/prenosni_kompjuteri.nspx?items=100&page=1");
                WebDriverWait wait = new WebDriverWait(driverCh, 100);
                List<WebElement> firstResult = wait.until(presenceOfAllElementsLocatedBy(By.className("product-list-item-grid")));

                String page = driverCh.getPageSource();
                doc = Jsoup.parse(page);
            }
            else {
                doc = Jsoup.connect(url).header("Accept-Encoding", "gzip, deflate")
                        .userAgent(" Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36 ")
                        .maxBodySize(0).timeout(0).get();
            }



            String pageAsXml = null;

            Elements links = new Elements(1000);
            Elements img = new Elements(1000);
            ArrayList<String> name = new ArrayList<>(1000);
            ArrayList<String> price = new ArrayList<>(1000);
            ArrayList<String> clubPrice = new ArrayList<>(1000);
            ArrayList<String> clubRate = new ArrayList<>(1000);
            Elements all;
            if(url.contains("setec")) {
                all = doc.getElementsByClass("product");
                for (Element el : all) {
                    if(el.getElementsByClass("name").first().getElementsByTag("a").text().contains("SAMSUNG QE50Q60TAUXXH"))
                        continue;
                    Element e = el.getElementsByTag("a").first();
                    links.add(e);
                    e = el.getElementsByTag("img").first();
                    img.add(e);
                    e = el.getElementsByClass("name").first();
                    name.add(e.getElementsByTag("a").text());
                    e = el.getElementsByClass("price-old-new").first();

                    if(e!= null) {
                        price.add("Редовна цена: "+e.text());
                    }
                    else
                        price.add("Редовна цена: 0 ден.");
                    e = el.getElementsByClass("price-new-new").first();
                    if(e!= null) {
                        clubPrice.add("Клуб цена: "+e.text());
                    }
                    else
                        clubPrice.add("Клуб цена: 0 ден.");
                    e = el.getElementsByClass("klub-rata-suma").first();
                    clubRate.add("24x Клуб рата: "+e.text());


                }
            }
            else {

                all = doc.select("div[class*=product-list-item-grid]");
                String p="haPPy";
                for (Element el : all) {
                    Element e = el.getElementsByTag("a").first();
                    if(e.attr("ng-href").contains("DELL-Inspiron-3584-i3-7020U-4GB-1TB-520-2GB") ||
                            e.attr("ng-href").contains("ASUS-X543MA-WBP01T-N5000-4GB-256GB-SSD-WIN-10") ||
                            e.attr("ng-href").contains("ASUS-X509JB-WB311-i3-1005G1-8GB-256GB-SSD-MX-110-2GB") ||
                            e.attr("ng-href").contains("LENOVO-Flex-5-14--i3-1005G1-8GB-256GB-SSD-WIN10-81X10096RM") ||
                            e.attr("ng-href").contains("LENOVO-Thinkpad-E14-14--i7-10510U-16GB-512GB-SSD20RA002URI"))
                        continue;
                    links.add(e);
                    e = el.getElementsByTag("img").first();

                    img.add(e);
                    name.add(el.getElementsByTag("h2").text());
                    e=el.select("div[class*=Regular]").select("span[class*=product-price__amount--value ng-binding]").first();
                    if(e!= null) {
                        price.add("Редовна цена: "+e.text()+" ден.");
                    }
                    else
                        price.add("Редовна цена: 0 ден.");
                    if(el.select("div[class=HappyCard]").select("span[class*=product-price__amount--value]").first()!= null) {
                        e=el.select("div[class=HappyCard]").select("span[class*=product-price__amount--value]").first();
                        clubPrice.add("haPPy цена: "+e.text()+" ден.");
                    }
                    else {
                        clubPrice.add("haPPy цена: 0 ден.");
                        p="Редовна";
                    }
                    e=el.select("span[class*=product-price__amount--value ng-binding ng-scope]").first();
                    clubRate.add("48x "+p+" рата: "+e.text());
                    p="haPPy";

                }
            }



            // Update results
            synchronized (lock) {
                int i=0;
                for (Element link : links) {
                    if (link.absUrl("href").contains("setec")) {
                        if (link.absUrl("href").contains("product_id")) {
                            productUrls.add(link.absUrl("href"));
//                            productImgs.add(img.get(i).absUrl("data-echo"));
                            productImgs.add(img.get(i).absUrl("src"));
                            productNames.add(name.get(i));
                            productPrices.add(price.get(i));
                            productClubPrices.add(clubPrice.get(i));
                            productPricePartial.add(clubRate.get(i));
                            i++;
                        }

                    }
                    if (link.hasAttr("ng-href")==true) {
                        productUrls.add("https://www.neptun.mk/"+link.attr("ng-href"));
                        productImgs.add("https://www.neptun.mk/"+img.get(i).attr("ng-src"));
                        productNames.add(name.get(i));
                        productPrices.add(price.get(i));
                        productClubPrices.add(clubPrice.get(i));
                        productPricePartial.add(clubRate.get(i));
                        i++;
                    }
                }
                this.results = links;
                lock.notifyAll();
            }
        } catch (IOException e) {
            System.err.println("Error while parsing: "+this.url);
            e.printStackTrace();
        }
    }

    public ArrayList<String> waitForResults() {
        synchronized (lock) {
            try {
                while (this.results == null) {
                    lock.wait();
                }
                return this.productUrls;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            return null;


        }
    }
}

