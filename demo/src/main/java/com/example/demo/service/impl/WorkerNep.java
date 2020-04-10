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

@Data
public class WorkerNep implements Runnable {

    private String url;
    private Elements results;
    private ArrayList<String> productUrls;
    private ArrayList<String> productImgs;
    private ArrayList<String> productNames;
    private String name;
    private static int number = 0;

    private final Object lock = new Object();

    public WorkerNep(String url) {
        this.url = url;
        this.name = "Worker-" + (number++);
        productUrls=new ArrayList<>();
        productImgs=new ArrayList<>();
        productNames=new ArrayList<>();
    }


    @Override
    public void run() {
        try {
            java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(java.util.logging.Level.OFF);
            java.util.logging.Logger.getLogger("org.apache.http").setLevel(java.util.logging.Level.OFF);

            WebClient webClient = new WebClient(BrowserVersion.FIREFOX_68);
            webClient.getOptions().setJavaScriptEnabled(true);
            webClient.setJavaScriptErrorListener(new SilentJavaScriptErrorListener());
            webClient.getOptions().setCssEnabled(false);
            webClient.getOptions().setThrowExceptionOnScriptError(false);
            webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
            webClient.getOptions().setTimeout(10 * 1000);
            webClient.setAjaxController(new NicelyResynchronizingAjaxController());


            HtmlPage page = webClient.getPage(url);
            webClient.waitForBackgroundJavaScript(100 * 1000);

            String pageAsXml = page.asXml();

            Document doc = Jsoup.parse(pageAsXml);


            Elements links = new Elements(1000);
            Elements img = new Elements(1000);
            ArrayList<String> name = new ArrayList<>(1000);
            Elements all;
            if(url.contains("http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100&page=")) {
                all = doc.getElementsByClass("product");
                for (Element el : all) {
                    Element e = el.getElementsByTag("a").first();
                    links.add(e);
                    e = el.getElementsByTag("img").first();
                    img.add(e);
                    e = el.getElementsByClass("name").first();
                    name.add(e.getElementsByTag("a").text());
                }
            }
            else {
                all = doc.select("div[class*=product-list-item-grid]");
                for (Element el : all) {
                    Element e = el.getElementsByTag("a").first();
                    links.add(e);
                    e = el.getElementsByTag("img").first();
                    img.add(e);
                    name.add(el.getElementsByTag("h2").text());

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
                            i++;
                        }

                    }
                    if (link.hasAttr("ng-href")==true) {
                        productUrls.add("https://www.neptun.mk/"+link.attr("ng-href"));
                        productImgs.add("https://www.neptun.mk/"+img.get(i).attr("ng-src"));
                        productNames.add(name.get(i));
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