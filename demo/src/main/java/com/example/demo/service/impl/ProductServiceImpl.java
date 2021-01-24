package com.example.demo.service.impl;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;
import com.example.demo.model.ProductDto;
import com.example.demo.model.Similar;
import com.example.demo.model.exceptions.InvalidProductIdException;
import com.example.demo.repository.ProductDetailsRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.SimilarRepository;
import com.example.demo.service.ProductService;

//import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import com.example.demo.util.WorkerDesc;
import com.example.demo.util.WorkerNep;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.xml.crypto.dsig.SignedInfo;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDetailsRepository productDetailsRepository;
    private final SimilarRepository similarRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductDetailsRepository productDetailsRepository, SimilarRepository similarRepository) {
        this.productRepository = productRepository;
        this.productDetailsRepository = productDetailsRepository;
        this.similarRepository = similarRepository;
    }

    public List<WorkerNep> workers;

    public WorkerNep w1;
    public WorkerNep w2;
    public WorkerNep w3;

    @PostConstruct
    @Override
    public void loadContents() throws FileNotFoundException {

        String[] urls = new String[]{
                "http://setec.mk/index.php?route=product/category&path=10002_10003&limit=100&page=",
                "https://www.neptun.mk/pocetna/categories/KOMPJUTERI/prenosni_kompjuteri.nspx?items=100&page="};

        workers = new ArrayList<>(urls.length);
        for (String url : urls) {
            w1 = new WorkerNep(url + "1");
//            w2 = new WorkerNep(url + "2");

            if (url.contains("setec")) {
                w2 = new WorkerNep(url + "2");
                w3 = new WorkerNep(url + "3");
                workers.add(w2);
                workers.add(w3);
                new Thread(w2).start();
                new Thread(w3).start();
            }
            workers.add(w1);
//            workers.add(w2);
//            new Thread(w2).start();
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
        ArrayList<String> fullDescRepo = new ArrayList<>(500);


        // Retrieve results
        for (WorkerDesc w : workers2) {
            ArrayList<String> prodd = w.waitForResults();
            if (prodd != null) {
                productDesc.addAll(prodd);
                fullDescRepo.addAll(w.getProductFullDesc());
            } else
                System.err.println(w.getName() + " had some error!");
        }


        for (int i = 0; i < productRepo.size(); i++) {
            ArrayList<String> properties = getProperties(productDesc.get(i));
            Product product = createProduct(nameRepo.get(i),
                    productRepo.get(i),
                    imgRepo.get(i),
                    properties.toString().substring(1, properties.toString().length() - 1),
                    priceRepo.get(i),
                    clubPriceRepo.get(i),
                    pricePartialRepo.get(i),
                    fullDescRepo.get(i));

            // ovde da ja povikuvame funkcijata so ke vrakjat lista od properties za dadeno description vo properties lista
            productDetailsRepository.save(properties.get(0), properties.get(1), properties.get(2), properties.get(3), properties.get(4), properties.get(5), product);


        }

        Scanner in = new Scanner(new FileInputStream("C:\\Users\\Magdalena\\OneDrive\\Web Programming\\WebProject\\demo\\similarities.csv"));
        while (in.hasNextLine()) {
            String[] parts = in.nextLine().split("\t");
            similarRepository.save(new Similar(Long.parseLong(parts[0]), Long.parseLong(parts[1]), false));
        }

    }


    public static ArrayList<String> getProperties(String description) {
        String[] properties = description.split("\\|");
        String toReturn;

        ArrayList<String> propertiesToReturn = new ArrayList<>();
        String[] tmp = null;
        int fl = 0;
        int fl1 = 0;
        int fl2 = 0;
        int fl3 = 0;
        int fl4 = 0;

        String display = "Not found";
        String graphicsCard = "Not found";
        String internalMemory = "Not found";
        String memory = "Not found";
        String processor = "Not found";
        String resolution = "Not found";

        for (String property : properties) {
            if (property.matches("(.*)[nN][vV][iI][dD][iI][aA](.*)|(.*)NVDIA(.*)|( *)GeForce(.*)|(.*)Itegrated VGA(.*)|(.*)Intel UHD (620|605)(.*)|(.*)Integrated Graphic Card(.*)|((.*)(Intel|AMD)(.*)[gG]raphics(.*))|(.*)Integrated(.*)VGA(.*)|(.*)Intel(.*)VGA(.*)|(.*)R[aA][dD][eE][oO][nN](.*)|(.*)Integrated graphics UMA(.*)|(.*)V[eE][gG][aA](.*)|(.*)Intel HD 620(.*)|(.*)UMA graphics(.*)|(.*)HD Graphics 620(.*)")) {
                if (property.contains("processor") || property.contains("Processor") || property.contains("Core")) {
                    tmp = property.split(",");
                    if (tmp.length == 1)
                        tmp = tmp[0].split("z  ");
                    graphicsCard = (tmp[tmp.length - 1].trim());
                } else if (fl1 == 1) {
                    String temp = graphicsCard;
                    temp = temp + " + " + property;
                    graphicsCard = temp.trim();
                } else {
                    fl1 = 1;
                    graphicsCard = property.trim();
                }
                if (graphicsCard.contains("NVDIA")) {
                    graphicsCard = graphicsCard.replace("NVDIA", "nVidia");
                }
                String temp = graphicsCard.toLowerCase();
                if (temp.contains("vega") || temp.contains("itegrated") || temp.contains("integrated") || temp.contains("intel") || temp.contains("uma")) {
                    graphicsCard = "Integrated: " + graphicsCard;
                } else if (temp.contains("nvidia") | temp.contains("geforce") || temp.contains("radeon")) {
                    graphicsCard = "Dedicated: " + graphicsCard;

                }
            }
            if (property.matches("(.*)[0-9](.*)SSD(.*)|(.*)[0-9](.*)HDD(.*)|(.*)SSD(.*)[0-9](.*)|(.*)HDD(.*)[0-9](.*)|(.*)[rR][pP][mM](.*)|( *)(1TB|256GB)( *)|(.*)eMMC(.*)|(.*)SDD(.*)")) {
                if (fl2 == 1) {
                    String temp = memory;
                    if (!memory.trim().equals(property.trim()))
                        temp = temp + " +  " + property;
                    memory = temp.trim();
                } else {
                    fl2 = 1;
                    if (property.contains(",") && property.contains("/")) {
                        tmp = property.split("/");
                        for (String s : tmp) {
                            if (s.contains("SSD") || s.contains("HDD")) {
                                memory = s.split(",")[0].trim();
                            }

                        }
                    } else if (property.contains(",")) {
                        tmp = property.split(",");
                        for (String s : tmp) {
                            if (s.contains("SSD") || s.contains("HDD"))
                                memory = s.trim();
                        }
                    } else if (property.contains("/")) {
                        tmp = property.split("/");
                        for (String s : tmp) {
                            if (s.contains("SSD") || s.contains("HDD"))
                                memory = s.trim();
                        }
                    } else
                        memory = property.trim();
                }
                if (memory.contains("+")) {
                    memory = "TwoHDD: " + memory;
                }
                if (memory.contains("Т")) {
                    memory = memory.replace("Т", "T");
                }
            }
            if (property.matches("( *)(4|8|12|16)GB( *)|(.*)DDR4(.*)|(.*)LPDDR3(.*)|( *)4GB DDR3( *)|(.*) RAM(.*)")) {
                if (property.contains(",")) {
                    tmp = property.split(",");
                    for (String s : tmp) {
                        if (s.contains("RAM"))
                            internalMemory = s.trim();
                    }
                } else if (property.contains("Intel")) {
                    tmp = property.split("s");
                    internalMemory = tmp[1].trim();

                } else {
                    internalMemory = property.trim();

                }
            }
            if (property.matches("(.*)(Core|Pentium|Celeron|Atom|Athlon|AMD R5|Ryzen|A4-9120C|E2124G)(.*)|( *)(i7-8565U|i3-7020U)(.*)")) {
                if (fl == 1) {
                    processor = property.trim();
                } else {
                    fl = 1;
                    processor = property.trim();
                }
                if (processor.contains("(") && processor.trim().indexOf("(") == 0)
                    processor = processor.replaceAll("[()]", "");
                if (processor.contains(","))
                    processor = processor.substring(0, processor.indexOf(","));
                if (processor.contains("("))
                    processor = processor.substring(0, processor.indexOf("("));
                if (processor.contains(".") && processor.indexOf(".") == 0)
                    processor = processor.substring(1);
                processor = processor.replaceAll("&trade;|&reg;|[)]", "");
                processor = processor.trim();
            }
            if (property.matches(".*[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}.*|.*Dimensions.*")) {
                Pattern p = Pattern.compile("[0-9]\\.?[0-9]{2,3}\\s{0,2}[xX]\\s{0,2}[0-9]\\.?[0-9]{2,3}");
                Matcher mt = p.matcher(property);
                if (mt.find()) {
                    if (fl3 == 1) {
                        resolution = mt.group();
                    } else {
                        fl3 = 1;
                        resolution = mt.group();
                    }
                }
            } else if (property.matches("\\s+[0-9]{3,4}-[0-9]{3,4}\\s+")) {
                toReturn = property.replace("-", "x").trim();
                resolution = toReturn;
            }

            if (property.matches("(.*)[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\"|\\&quot;|-( *)i)(.*)|(.*)14.0(.*)|( *)15.6(.*)")) {
                Pattern p = Pattern.compile("[1-9][0-9]( *)(,|\\.)*( *)[0-9]*( *)(\"|\\&quot;|-( *)i)|14.0|15.6");
                String temp = "";
                Matcher mt = p.matcher(property);
                if (mt.find()) {
                    temp = mt.group();
                    if (mt.group().contains("quot;"))
                        temp = mt.group().split("\\&")[0];
                    else if (mt.group().contains("-i"))
                        temp = mt.group().split("-")[0];
                    if (fl4 == 1) {
                        display = temp;
                    } else {
                        fl4 = 1;
                        display = temp;
                    }
                }
                display = display.replace(",", ".");
            }
        }

        if (graphicsCard.equals("Not found"))
            graphicsCard = "Integrated: " + graphicsCard;


        propertiesToReturn.add(display);
        propertiesToReturn.add(graphicsCard);
        propertiesToReturn.add(internalMemory);
        propertiesToReturn.add(memory);
        propertiesToReturn.add(processor);
        propertiesToReturn.add(resolution);


        return propertiesToReturn;


    }

    public boolean haveSameDisplay(ProductDetails product_1, ProductDetails product_2) {
        if (!product_1.getId().equals(product_2.getId())) {
            String forComparing_1 = product_1.getDisplay().replaceAll("[^0-9.]", "");
            String forComparing_2 = product_2.getDisplay().replaceAll("[^0-9.]", "");
            return forComparing_1.equals(forComparing_2)
                    || (forComparing_1 + ".0").equals(forComparing_2)
                    || (forComparing_2 + ".0").equals(forComparing_1);
        }
        return false;

    }

    public boolean haveSameResolution(ProductDetails product_1, ProductDetails product_2) {
        if (!product_1.getId().equals(product_2.getId())) {

            String forComparing_1 = product_1.getResolution().replaceAll("\\s+", "").replace("X", "x");
            String forComparing_2 = product_2.getResolution().replaceAll("\\s+", "").replace("X", "x");
            return forComparing_1.equals(forComparing_2);
        }
        return false;

    }

    public boolean haveSameInternalMemory(ProductDetails product_1, ProductDetails product_2) {
        if (!product_1.getId().equals(product_2.getId())) {

            String forComparing_1 = product_1.getInternalMemory().split("[Gg]")[0].replaceAll("[^0-9]", "");
            String forComparing_2 = product_2.getInternalMemory().split("[Gg]")[0].replaceAll("[^0-9]", "");
            return forComparing_1.equals(forComparing_2);
        }
        return false;

    }

    public boolean haveSameGraphicsCard(ProductDetails product_1, ProductDetails product_2) {
        if (!product_1.getId().equals(product_2.getId())) {

            if (product_1.getGraphicsCard().contains("Integrated: ") && product_2.getGraphicsCard().contains("Integrated: ")) {
                if (product_1.getGraphicsCard().toLowerCase().contains("intel")
                        && product_2.getGraphicsCard().toLowerCase().contains("intel"))
                    return true;
                else if (product_1.getGraphicsCard().toLowerCase().contains("vega")
                        && product_2.getGraphicsCard().toLowerCase().contains("vega"))
                    return true;
                else if (product_1.getGraphicsCard().toLowerCase().contains("uma")
                        && product_2.getGraphicsCard().toLowerCase().contains("uma"))
                    return true;
                else return product_1.getGraphicsCard().toLowerCase().matches("(.*)(itegrated|integrated)(.*)vga(.*)")
                            && product_2.getGraphicsCard().toLowerCase().matches("(.*)(itegrated|integrated)(.*)vga(.*)");
            } else if (product_1.getGraphicsCard().contains("Dedicated: ") && product_2.getGraphicsCard().contains("Dedicated: ")) {
                if (product_1.getGraphicsCard().toLowerCase().contains("radeon")
                        && product_2.getGraphicsCard().toLowerCase().contains("radeon"))
                    return true;
                else return product_1.getGraphicsCard().toLowerCase().matches("(.*)(nvidia|geforce)(.*)")
                        && product_2.getGraphicsCard().toLowerCase().matches("(.*)(nvidia|geforce)(.*)");
            }
            return false;
        }
        return false;
    }

    public boolean haveSameProcessor(ProductDetails product_1, ProductDetails product_2) {
        if (!product_1.getId().equals(product_2.getId())) {

            if (product_1.getProcessor().toLowerCase().contains("i3") &&
                    product_2.getProcessor().toLowerCase().contains("i3"))
                return true;
            else if (product_1.getProcessor().toLowerCase().contains("i5") &&
                    product_2.getProcessor().toLowerCase().contains("i5"))
                return true;
            else if (product_1.getProcessor().toLowerCase().contains("i7") &&
                    product_2.getProcessor().toLowerCase().contains("i7"))
                return true;
            else if (product_1.getProcessor().toLowerCase().contains("amd") &&
                    product_2.getProcessor().toLowerCase().contains("amd")) {
                if ((product_1.getProcessor().toLowerCase().matches("(.*) 3 (.*)") || product_1.getProcessor().toLowerCase().matches("(.*)r3(.*)")) &&
                        (product_2.getProcessor().toLowerCase().matches("(.*) 3 (.*)") || product_2.getProcessor().toLowerCase().matches("(.*)r3(.*)"))) {
                    return true;
                } else if ((product_1.getProcessor().toLowerCase().matches("(.*) 5 (.*)") || product_1.getProcessor().toLowerCase().matches("(.*)r5(.*)")) &&
                        (product_2.getProcessor().toLowerCase().matches("(.*) 5 (.*)") || product_2.getProcessor().toLowerCase().matches("(.*)r5(.*)"))) {
                    return true;
                } else if ((product_1.getProcessor().toLowerCase().matches("(.*) 7 (.*)") || product_1.getProcessor().toLowerCase().matches("(.*)r7(.*)")
                        || product_1.getProcessor().toLowerCase().matches("(.*) 7-(.*)") || product_1.getProcessor().toLowerCase().matches("(.*)ryzen7(.*)")) &&
                        (product_2.getProcessor().toLowerCase().matches("(.*) 7 (.*)") || product_2.getProcessor().toLowerCase().matches("(.*)r7(.*)")
                                || product_2.getProcessor().toLowerCase().matches("(.*) 7-(.*)") || product_2.getProcessor().toLowerCase().matches("(.*)ryzen7(.*)"))) {
                    return true;
                } else if (product_1.getProcessor().toLowerCase().matches("(.*) a4(.*)") &&
                        product_2.getProcessor().toLowerCase().matches("(.*) a4(.*)")) {
                    return true;
                }

            } else if (product_1.getProcessor().toLowerCase().contains("celeron") &&
                    product_2.getProcessor().toLowerCase().contains("celeron"))
                return true;
            else if (product_1.getProcessor().toLowerCase().contains("pentium") &&
                    product_2.getProcessor().toLowerCase().contains("pentium"))
                return true;
            else return product_1.getProcessor().toLowerCase().contains("atom") &&
                        product_2.getProcessor().toLowerCase().contains("atom");
        }
        return false;
    }

    public boolean haveSameMemory(ProductDetails product_1, ProductDetails product_2) {
        if (!product_1.getId().equals(product_2.getId())) {

            if (product_1.getMemory().contains("TwoHDD") && product_2.getMemory().contains("TwoHDD")) {
                if (product_1.getMemory().contains("1TB") && product_2.getMemory().contains("1TB")) {
                    Pattern p = Pattern.compile("(\\d\\d\\dG)|(\\d\\dG)|(\\dG)");
                    Matcher m_1 = p.matcher(product_1.getMemory());
                    Matcher m_2 = p.matcher(product_2.getMemory());
                    if (m_1.find() && m_2.find()) {
                        return m_1.group().equals(m_2.group());
                    }
                }
            } else if (!product_1.getMemory().contains("TwoHDD")
                    && !product_2.getMemory().contains("TwoHDD")) {
                if (product_1.getMemory().matches("(.*)1TB(.*)(HDD|(.*)rpm(.*))")
                        && product_2.getMemory().matches("(.*)1TB(.*)(HDD|(.*)rpm(.*))")) {
                    return true;
                } else if (product_1.getMemory().matches("(.*)2TB(.*)(HDD|(.*)rpm(.*))")
                        && product_2.getMemory().matches("(.*)2TB(.*)(HDD|(.*)rpm(.*))")) {
                    return true;
                } else if (product_1.getMemory().matches("(.*)1TB(.*)SSD(.*)")
                        && !product_1.getMemory().contains("HDD")
                        && !product_2.getMemory().contains("HHD")
                        && product_2.getMemory().matches("(.*)1TB(.*)SSD(.*)")) {
                    return true;
                } else {
                    Pattern p = Pattern.compile("\\d\\d\\d");
                    Matcher m_1 = p.matcher(product_1.getMemory());
                    Matcher m_2 = p.matcher(product_2.getMemory());
                    if (m_1.find() && m_2.find()) {
                        if (m_1.group().equals(m_2.group())) {
                            return true;
                        }
                    } else {
                        p = Pattern.compile("\\d\\d");
                        m_1 = p.matcher(product_1.getMemory());
                        m_2 = p.matcher(product_2.getMemory());
                        if (m_1.find() && m_2.find()) {
                            if (m_1.group().equals(m_2.group())) {
                                return true;
                            }
                        }
                    }
                }
            }

            return false;
        }
        return false;
    }

    public Product getMostSimilarPrice(Product product_1, List<Product> products) {

        int mainPrice = Integer.parseInt(product_1.getPrice().replaceAll("[^0-9]", ""));
        int mostSimilarPrice = Integer.parseInt(products.get(0).getPrice().replaceAll("[^0-9]", ""));
        Product mostSimilarProductByPrice = products.get(0);
        for (Product product : products) {
            if (!product.getId().equals(product_1.getId())) {
                int difference = Math.abs(Integer.parseInt(product.getPrice().replaceAll("[^0-9]", "")) - mainPrice);
                if (difference < mostSimilarPrice) {
                    mostSimilarPrice = difference;
                    mostSimilarProductByPrice = product;
                }
            }
        }
        return mostSimilarProductByPrice;
    }

    public int findMaxSimilarities(ProductDetails product_1, List<ProductDetails> products) {
        int similarities = 0;
        int maxSimilarities = 0;
        for (ProductDetails product : products) {
            if (!product_1.getId().equals(product.getId())) {

                if (haveSameDisplay(product_1, product)) similarities += 1;
                if (haveSameGraphicsCard(product_1, product)) similarities += 1;
                if (haveSameInternalMemory(product_1, product)) similarities += 2;
                if (haveSameMemory(product_1, product)) similarities += 2;
                if (haveSameProcessor(product_1, product)) similarities += 3;
                if (haveSameResolution(product_1, product)) similarities += 1;
                System.out.println(similarities);
                if (similarities > maxSimilarities) {
                    maxSimilarities = similarities;
                }
            }
            similarities = 0;
        }
        return maxSimilarities;
    }

    public Product findMostSimilarProduct(ProductDetails product_1, List<ProductDetails> products) {
        int maxSimilarities = findMaxSimilarities(product_1, products);
        System.out.println(maxSimilarities);
        int similarities = 0;
        List<ProductDetails> productsWithSameNumberSimilarities = new ArrayList<>();
        for (ProductDetails product : products) {
            if (!product_1.getId().equals(product.getId())) {

                if (haveSameDisplay(product_1, product)) similarities += 1;
                if (haveSameGraphicsCard(product_1, product)) similarities += 1;
                if (haveSameInternalMemory(product_1, product)) similarities += 2;
                if (haveSameMemory(product_1, product)) similarities += 2;
                if (haveSameProcessor(product_1, product)) similarities += 3;
                if (haveSameResolution(product_1, product)) similarities += 1;
                if (similarities == maxSimilarities) {
                    productsWithSameNumberSimilarities.add(product);
                }
            }
            similarities = 0;
        }
        List<Product> productList = productsWithSameNumberSimilarities.stream().map(ProductDetails::getProduct).collect(Collectors.toList());
        return getMostSimilarPrice(product_1.getProduct(), productList);
    }

    @Override
    public List<Product> listProducts() {
        return this.productRepository.getAllProducts();
    }

    @Override
    public Product createProduct(String name, String url, String imgUrl, String description, String price, String clubPrice, String pricePartial, String fullDescription) {
        Product product = new Product(name, url, imgUrl, description, price, clubPrice, pricePartial, fullDescription);
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

    @Override
    public List<ProductDetails> searchProducts(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution) {
        return this.productDetailsRepository.searchProducts(display, graphicsCard, internalMemory, memory, processor, resolution);
    }

    @Override
    public List<Product> findByUrlContains(String store) {
        return productRepository.findByUrlContains(store);
    }

    @Override
    public Similar updateSimilarProduct(Long id, Long similar) {
        return this.similarRepository.updateMostSimilar(id, similar).get(0);
    }

    @Override
    public List<ProductDto> getProductAndSimilarProducts(Long id) {
        List<ProductDto> toReturn = new ArrayList<>();
        List<Similar> similar = this.similarRepository.getByProductId(id);
        ProductDto currentProduct = new ProductDto(getProduct(id), (long) -1);
        toReturn.add(currentProduct);
        for (Similar s: similar) {
            if (s.getMostSimilar()) {
                currentProduct.setMostSimilarId(s.getSecondProduct());
            }
            toReturn.add(new ProductDto(getProduct(s.getSecondProduct()), (long) 0));
        }
        return toReturn;
    }

//    @Override
//    public Product updateSimilarProduct(Long id, Product similar) {
//        Product product = this.productRepository.findById(id).orElseThrow(InvalidProductIdException::new);
//        product.setSimilarId(similar.getId());
//        product.setSimilarName(similar.getName());
//        product.setSimilarUrl(similar.getUrl());
//        product.setSimilarDescription(similar.getDescription());
//        product.setSimilarPrice(similar.getPrice());
//        return this.productRepository.save(product);
//    }


}