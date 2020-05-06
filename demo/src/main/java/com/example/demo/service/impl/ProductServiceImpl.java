package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.model.exceptions.InvalidProductIdException;
import com.example.demo.repository.ProductDetailsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;

//import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import com.example.demo.util.WorkerDesc;
import com.example.demo.util.WorkerNep;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            ArrayList<String> properties=getProperties(productDesc.get(i));
            productDetailsRepository.save(properties.get(0), properties.get(1), properties.get(2), properties.get(3), properties.get(4), properties.get(5), product);



        }


    }



    public static  ArrayList<String> getProperties(String description) {
        String[] properties = description.split("\\|");
        String toReturn;

        ArrayList<String> propertiesToReturn=new ArrayList<>();
        String[] tmp=null;
        int fl=0;
        int fl1=0;
        int fl2=0;
        int fl3=0;
        int fl4=0;

        String display = "Not found";
        String graphicsCard = "Not found";
        String internalMemory = "Not found";
        String memory = "Not found";
        String processor = "Not found";
        String resolution = "Not found";

        for (String property : properties) {
            if (property.matches("(.*)[nN][vV][iI][dD][iI][aA](.*)|(.*)NVDIA(.*)|( *)GeForce(.*)|(.*)Itegrated VGA(.*)|(.*)Intel UHD (620|605)(.*)|(.*)Integrated Graphic Card(.*)|((.*)(Intel|AMD)(.*)[gG]raphics(.*))|(.*)Integrated(.*)VGA(.*)|(.*)Intel(.*)VGA(.*)|(.*)R[aA][dD][eE][oO][nN](.*)|(.*)Integrated graphics UMA(.*)|(.*)V[eE][gG][aA](.*)|(.*)Intel HD 620(.*)|(.*)UMA graphics(.*)|(.*)HD Graphics 620(.*)")) {
                if (property.contains("processor") || property.contains("Processor")|| property.contains("Core")) {
                    tmp = property.split(",");
                    if (tmp.length == 1)
                        tmp = tmp[0].split("z  ");
                    graphicsCard = (tmp[tmp.length - 1].trim());
                } else  if(fl1==1){
                    String temp=graphicsCard;
                    temp = temp + " + "+property;
                    graphicsCard=temp.trim();
                }
                else {
                    fl1 = 1;
                    graphicsCard=property.trim();
                }
            }
            if (property.matches("(.*)[0-9](.*)SSD(.*)|(.*)[0-9](.*)HDD(.*)|(.*)SSD(.*)[0-9](.*)|(.*)HDD(.*)[0-9](.*)|(.*)[rR][pP][mM](.*)|( *)(1TB|256GB)( *)|(.*)eMMC(.*)|(.*)SDD(.*)")) {
                if (fl2 == 1) {
                    String temp = memory;
                    temp = temp + " + " + property;
                    memory=temp.trim();
                } else {
                    fl2 = 1;
                    if (property.contains(",") && property.contains("/")) {
                        tmp = property.split("/");
                        for (String s : tmp) {
                            if (s.contains("SSD") || s.contains("HDD")) {
                                memory=s.split(",")[0].trim();
                            }

                        }
                    } else if (property.contains(",")) {
                        tmp = property.split(",");
                        for (String s : tmp) {
                            if (s.contains("SSD") || s.contains("HDD"))
                                memory=s.trim();
                        }
                    } else if (property.contains("/")) {
                        tmp = property.split("/");
                        for (String s : tmp) {
                            if (s.contains("SSD") || s.contains("HDD"))
                                memory=s.trim();
                        }
                    } else
                        memory=property.trim();
                }
            }
            if (property.matches("( *)(4|8|12|16)GB( *)|(.*)DDR4(.*)|(.*)LPDDR3(.*)|( *)4GB DDR3( *)|(.*) RAM(.*)")) {
                if (property.contains(",")) {
                    tmp = property.split(",");
                    for (String s : tmp) {
                        if (s.contains("RAM"))
                            internalMemory=s.trim();
                    }
                } else if (property.contains("Intel")) {
                    tmp = property.split("s");
                    internalMemory=tmp[1].trim();

                }else {
                    internalMemory=property.trim();

                }
            }
            if (property.matches("(.*)(Core|Pentium|Celeron|Atom|Athlon|AMD R5|Ryzen|A4-9120C|E2124G)(.*)|( *)(i7-8565U|i3-7020U)(.*)")) {
                if(fl==1){
                    processor=property.trim();
                }
                else {
                    fl = 1;
                    processor=property.trim();
                }
            }
            if (property.matches(".*[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}.*|.*Dimensions.*")) {
                Pattern p = Pattern.compile("[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}");
                Matcher mt = p.matcher(property);
                if (mt.find()) {
                    if (fl3 == 1) {
                        resolution=mt.group();
                    } else {
                        fl3 = 1;
                        resolution=mt.group();
                    }
                }
            }else if(property.matches("\\s+[0-9]{3,4}-[0-9]{3,4}\\s+")){
                toReturn = property.replace("-", "x").trim();
                resolution=toReturn;
            }

            if (property.matches("(.*)[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\"|\\&quot;|-( *)i)(.*)|(.*)14.0(.*)|( *)15.6(.*)")) {
                Pattern p = Pattern.compile("[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\"|\\&quot;|-( *)i)|14.0|15.6");
                String temp="";
                Matcher mt = p.matcher(property);
                if (mt.find()) {
                    temp=mt.group();
                    if(mt.group().contains("quot;"))
                        temp=mt.group().split("\\&")[0];
                    else if(mt.group().contains("-i"))
                        temp=mt.group().split("-")[0];
                    if (fl4 == 1) {
                        display=temp;
                    } else {
                        fl4 = 1;
                        display=temp;
                    }
                }
            }
        }
        propertiesToReturn.add(display);
        propertiesToReturn.add(graphicsCard);
        propertiesToReturn.add(internalMemory);
        propertiesToReturn.add(memory);
        propertiesToReturn.add(processor);
        propertiesToReturn.add(resolution);



        return propertiesToReturn;

//        for (int j = 0; j <prod.length ; j++) {
//            if (prod[j].matches("(.*)[nN][vV][iI][dD][iI][aA](.*)|(.*)NVDIA(.*)|( *)GeForce(.*)|(.*)Itegrated VGA(.*)|(.*)Intel UHD (620|605)(.*)|(.*)Integrated Graphic Card(.*)|((.*)(Intel|AMD)(.*)[gG]raphics(.*))|(.*)Integrated(.*)VGA(.*)|(.*)Intel(.*)VGA(.*)|(.*)R[aA][dD][eE][oO][nN](.*)|(.*)Integrated graphics UMA(.*)|(.*)V[eE][gG][aA](.*)|(.*)Intel HD 620(.*)|(.*)UMA graphics(.*)|(.*)HD Graphics 620(.*)")) {
//                if (prod[j].contains("processor") || prod[j].contains("Processor")|| prod[j].contains("Core")) {
//                    tmp = prod[j].split(",");
//                    if (tmp.length == 1)
//                        tmp = tmp[0].split("z  ");
//                    gpu.add(tmp[tmp.length - 1].trim());
//                } else  if(fl1==1){
//                    System.out.println(prod[j]);
//                    String temp=gpu.get(gpu.size()-1);
//                    gpu.remove(gpu.size()-1);
//                    temp = temp + " + "+prod[j];
//                    gpu.add(temp.trim());
//                }
//                else {
//                    fl1 = 1;
//                    gpu.add(prod[j].trim());
//                }
//            }
//            if (prod[j].matches("(.*)[0-9](.*)SSD(.*)|(.*)[0-9](.*)HDD(.*)|(.*)SSD(.*)[0-9](.*)|(.*)HDD(.*)[0-9](.*)|(.*)[rR][pP][mM](.*)|( *)(1TB|256GB)( *)|(.*)eMMC(.*)|(.*)SDD(.*)")) {
//                if (fl2 == 1) {
//                    System.out.println(prod[j]);
//                    String temp = m.get(m.size() - 1);
//                    m.remove(m.size() - 1);
//                    temp = temp + " + " + prod[j];
//                    m.add(temp.trim());
//                } else {
//                    fl2 = 1;
//                    if (prod[j].contains(",") && prod[j].contains("/")) {
//                        System.out.println(prod[j]);
//                        tmp = prod[j].split("/");
//                        for (String s : tmp) {
//                            if (s.contains("SSD") || s.contains("HDD")) {
////                                    s.split(",")[0]
//                                m.add(s.split(",")[0].trim());
//                                System.out.println(s.split(",")[0]);
//                            }
//
//                        }
//                    } else if (prod[j].contains(",")) {
//                        System.out.println(prod[j]);
//                        tmp = prod[j].split(",");
//                        for (String s : tmp) {
//                            if (s.contains("SSD") || s.contains("HDD"))
//                                m.add(s.trim());
//                        }
//                    } else if (prod[j].contains("/")) {
//                        System.out.println(prod[j]);
//                        tmp = prod[j].split("/");
//                        for (String s : tmp) {
//                            if (s.contains("SSD") || s.contains("HDD"))
//                                m.add(s.trim());
//                        }
//                    } else
//                        m.add(prod[j].trim());
//                }
//            }
//            if (prod[j].matches("^( *)(4|8|12|16)GB  $|(.*)DDR4(.*)|(.*)LPDDR3(.*)|( *)4GB DDR3( *)|(.*) RAM(.*)")) {
//                if (prod[j].contains(",")) {
//                    tmp = prod[j].split(",");
//                    for (String s : tmp) {
//                        if (s.contains("RAM"))
//                            im.add(s.trim());
//                    }
//                } else if (prod[j].contains("Intel")) {
//                    tmp = prod[j].split("s");
//
//                    im.add(tmp[1].trim());
//
//                }else {
//                    im.add(prod[j].trim());
//
//                }
//            }
//            if (prod[j].matches("(.*)(Core|Pentium|Celeron|Atom|Athlon|AMD R5|Ryzen|A4-9120C|E2124G)(.*)|( *)(i7-8565U|i3-7020U)(.*)")) {
//                if(fl==1){
//                    System.out.println(prod[j]);
//                    cpu.remove(cpu.size()-1);
//                    cpu.add(prod[j].trim());
//                }
//                else {
//                    fl = 1;
//                    cpu.add(prod[j].trim());
//                }
//            }
//            if (prod[j].matches(".*[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}.*|.*Dimensions.*")) {
//                Pattern p = Pattern.compile("[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}");
//                Matcher mt = p.matcher(prod[j]);
//                if (mt.find()) {
//                    if (fl3 == 1) {
//                        System.out.println(mt.group());
//                        resolution.remove(resolution.size() - 1);
//                        resolution.add(mt.group());
//                    } else {
//                        fl3 = 1;
//                        resolution.add(mt.group());
//                    }
//                }
//            }else if(prod[j].matches("\\s+[0-9]{3,4}-[0-9]{3,4}\\s+")){
//                toReturn = prod[j].replace("-", "x").trim();
//                resolution.add(toReturn);
//            }
//
//            if (prod[j].matches("(.*)[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\"|\\&quot;|-( *)i)(.*)|(.*)14.0(.*)|( *)15.6(.*)")) {
////                    System.out.println(prod[j]);
//                Pattern p = Pattern.compile("[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\"|\\&quot;|-( *)i)|14.0|15.6");
////                    Pattern p = Pattern.compile("[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\")*");
//                String temp="";
//                Matcher mt = p.matcher(prod[j]);
//                if (mt.find()) {
//                    temp=mt.group();
//                    if(mt.group().contains("quot;"))
//                        temp=mt.group().split("\\&")[0];
//                    else if(mt.group().contains("-i"))
//                        temp=mt.group().split("-")[0];
//                    if (fl4 == 1) {
////                            System.out.println(mt.group());
//                        display.remove(display.size() - 1);
//                        display.add(temp);
//                    } else {
//                        fl4 = 1;
//                        display.add(temp);
//                    }
//                }
//            }
//        }
    }

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