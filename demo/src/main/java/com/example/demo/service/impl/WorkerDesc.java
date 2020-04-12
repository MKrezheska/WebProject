package com.example.demo.service.impl;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class WorkerDesc implements Runnable {

    private String url;
    private Element results;
    private ArrayList<String> productDesc;
    private String name;
    private static int number = 0;

    private final Object lock = new Object();

    public WorkerDesc(String url) {
        this.url = url;
        this.name = "Worker-" + (number++);
        productDesc=new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    @Override
    public void run() {


        Connection.Response response = null;
        Document doc=null;


        if (this.url.contains("setec")) {
            while (true){
                try {
                    response = Jsoup.connect(this.url).referrer("http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
                            .timeout(50000).execute();
                    doc=response.parse();
                    break;
                } catch (IOException e) {
                    if(response !=null) {
                        System.out.println(response.statusCode() + " : " + response.url());
                    }
                }

            }
        }
        else {
            try {
                doc = Jsoup.connect(this.url).header("Accept-Encoding", "gzip").referrer("http://google.com").userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.132 Safari/537.36")
                        .timeout(50000).get();
            } catch (IOException e) {
                System.err.println("Error while parsing: "+this.url);
                e.printStackTrace();            }

        }


        if(doc!=null) {
            Element desc;
            if (url.contains("setec"))
                desc = doc.getElementById("tab-description");
            else {
                desc = doc.selectFirst("meta[name='description']");

            }


            // Update results
            synchronized (lock) {
                if (url.contains("setec")) {
                    String[] parse = desc.text().split("\\)- | - |, -|,-|, ");
                    ArrayList<String> p = new ArrayList<>();
                    int bound = parse.length;
                    for (int i = 0; i < bound; i++) {
                        if (i + 1 != bound && parse[i].length() - 1 != -1) {
                            if (i != 0 && parse[i].contains("(") && !parse[i].contains(")")) {
                                p.add(parse[i] + " " + parse[i + 1]);
                                ++i;
                            } else {
                                p.add(parse[i]);
                            }
                        } else
                            p.add(parse[i]);
                    }
                    List<String> a = p.subList(1, p.size() - 1);
                    String inp = String.join(" | ", a).replaceAll("&quot;", "\"");
//                                String inp =p.toString();
                    productDesc.add(inp);
                }


                if (url.contains("neptun")) {
                    String[] parse = desc.attr("content").split(":|[\\r\\n]+");
                    ArrayList<String> p;
                    int bound = parse.length;
                    p = IntStream.range(0, bound).filter(i -> parse[i].length() - 1 != -1)
                            .filter(i -> !Character.UnicodeBlock.of(parse[i]
                                    .charAt(parse[i].length() - 1))
                                    .equals(Character.UnicodeBlock.CYRILLIC))
                            .filter(i -> !Character.UnicodeBlock.of(parse[i].charAt(0))
                                    .equals(Character.UnicodeBlock.CYRILLIC))
                            .mapToObj(i -> parse[i])
                            .collect(Collectors.toCollection(ArrayList::new));
                    String inp = String.join(" | ", p).replaceAll("&quot;", "\"");
                    productDesc.add(inp);


                }

                this.results = desc;
                lock.notifyAll();
            }
        }


    }

    public ArrayList<String> waitForResults() {
        synchronized (lock) {
            try {
                while (this.results == null) {
                    lock.wait();
                }
                return this.productDesc;
            } catch (InterruptedException e) {
                // Again better error handling
                e.printStackTrace();
            }


            return null;


        }
    }
}

