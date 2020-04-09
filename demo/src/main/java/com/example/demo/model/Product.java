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
    private Long id;

    @Column(name = "productName")
    private String name;

    @Column(name = "url")
    private String url;

    @Column(name = "imgUrl")
    private String imgUrl;

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
