package com.example.demo.service.impl;


import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.SilentJavaScriptErrorListener;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;

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


    @Override
    public void run() {
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_68);
//            webClient.getOptions().setJavaScriptEnabled(true);
            if (this.url.contains("neptun"))
                webClient.getOptions().setJavaScriptEnabled(true); // Enable JS interpreter, default is true
            else {
                webClient.getOptions().setJavaScriptEnabled(false); // Enable JS interpreter, default is true
            }
            webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setTimeout(10 * 1000);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());


            HtmlPage page = webClient.getPage(url);
//            webClient.waitForBackgroundJavaScript(100 * 1000);
            if (this.url.contains("neptun"))
                webClient.waitForBackgroundJavaScript(60 * 1000); // Wait for js to execute in the background for 30 seconds

            String pageAsXml = page.asXml();

            Document doc = Jsoup.parse(pageAsXml);


            Elements links = new Elements(1000);
            Elements img = new Elements(1000);
            ArrayList<String> name = new ArrayList<>(1000);
            ArrayList<String> price = new ArrayList<>(1000);
            ArrayList<String> clubPrice = new ArrayList<>(1000);
            ArrayList<String> pricePartial = new ArrayList<>(1000);
            Elements all;
            if(url.contains("setec")) {
                all = doc.getElementsByClass("product");
                for (Element el : all) {
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
                    pricePartial.add("24x Клуб рата: "+e.text());

                }
            }
            else {
                all = doc.select("div[class*=product-list-item-grid]");
                String p="haPPy";
                for (Element el : all) {
                    Element e = el.getElementsByTag("a").first();
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
//                         e1=el.select("div[class*=HappyCard]").get(1);
                        e=el.select("div[class=HappyCard]").select("span[class*=product-price__amount--value]").first();
                        clubPrice.add("haPPy цена: "+e.text()+" ден.");
                    }
                    else {
                        clubPrice.add("haPPy цена: 0 ден.");
                        p="Редовна";
                    }
                    e=el.select("span[class*=product-price__amount--value ng-binding ng-scope]").first();
                    pricePartial.add("48x "+p+" рата: "+e.text());
                    p="haPPy";
                }
            }



            synchronized (lock) {
                int i=0;
                for (Element link : links) {
                    if (link.absUrl("href").contains("setec")) {
                        if (link.absUrl("href").contains("product_id")) {
                            productUrls.add(link.absUrl("href"));
                            productImgs.add(img.get(i).absUrl("data-echo"));
                            productNames.add(name.get(i));
                            productPrices.add(price.get(i));
                            productClubPrices.add(clubPrice.get(i));
                            productPricePartial.add(pricePartial.get(i));
                            i++;
                        }

                    }
                    if (link.hasAttr("ng-href")==true) {
                        productUrls.add("https://www.neptun.mk/"+link.attr("ng-href"));
                        productImgs.add("https://www.neptun.mk/"+img.get(i).attr("ng-src"));
                        productNames.add(name.get(i));
                        productPrices.add(price.get(i));
                        productClubPrices.add(clubPrice.get(i));
                        productPricePartial.add(pricePartial.get(i));
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