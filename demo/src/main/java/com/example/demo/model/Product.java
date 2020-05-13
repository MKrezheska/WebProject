package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Entity
//@Table(name = "PRODUCTS")
@Table(name = "PRODUCTS",uniqueConstraints={@UniqueConstraint(columnNames={"url"})})
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "productName")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "imgUrl")
    private String imgUrl;

    @Column(name = "prodDesc", length = 2048)
    private String description;

    @Column(name = "price")
    private String price;

    @Column(name = "clubPrice")
    private String clubPrice;

    @Column(name = "pricePartial")
    private String pricePartial;

    public Product(String name, String url, String imgUrl, String description, String price, String clubPrice, String pricePartial) {
        this.name = name;
        this.url = url;
        this.imgUrl = imgUrl;
        this.description = description;
        this.price = price;
        this.clubPrice = clubPrice;
        this.pricePartial = pricePartial;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) &&
                Objects.equals(name, product.name) &&
                Objects.equals(url, product.url) &&
                Objects.equals(imgUrl, product.imgUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, url, imgUrl);
    }
}
