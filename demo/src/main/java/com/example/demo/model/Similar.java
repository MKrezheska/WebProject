package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "SIMILARITIES")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Similar {
    @Id
    @GeneratedValue
    private Long similar_id;

    @Column(name = "product_1")
    Long firstProduct;

    @Column(name = "product_2")
    Long secondProduct;

    @Column(name = "most_similar")
    Boolean mostSimilar;

    public Similar(Long firstProduct, Long secondProduct, Boolean mostSimilar) {
        this.firstProduct = firstProduct;
        this.secondProduct = secondProduct;
        this.mostSimilar = mostSimilar;
    }
}
