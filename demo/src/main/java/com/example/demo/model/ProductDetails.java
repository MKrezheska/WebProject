package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "PRODUCTS_DETAILS")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ProductDetails {

    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "display")
    private String display;

    @Column(name = "graphics_card")
    private String graphicsCard;

    @Column(name = "internal_memory")
    private String internalMemory;

    @Column(name = "memory")
    private String memory;

    @Column(name = "processor")
    private String processor;

    @Column(name = "resolution")
    private String resolution;

    @OneToOne
    private Product product;

    public ProductDetails(String display, String graphicsCard, String internalMemory, String memory, String processor, String resolution, Product product) {
        this.display = display;
        this.graphicsCard = graphicsCard;
        this.internalMemory = internalMemory;
        this.memory = memory;
        this.processor = processor;
        this.resolution = resolution;
        this.product = product;
    }
}
