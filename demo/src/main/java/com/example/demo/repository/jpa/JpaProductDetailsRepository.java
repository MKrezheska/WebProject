package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaProductDetailsRepository extends JpaRepository<ProductDetails, Long> {

    @Query("select c from ProductDetails c " +
            "WHERE c.display like %:display% " +
            "and c.graphicsCard like %:graphicsCard%" +
            "and c.internalMemory like %:internalMemory% " +
            "and c.memory like %:memory% " +
            "and c.processor like %:processor% " +
            "and c.resolution like %:resolution%")
    List<ProductDetails> searchProducts(@Param("display") String display,
                                        @Param("graphicsCard") String graphicsCard,
                                        @Param("internalMemory") String internalMemory,
                                        @Param("memory") String memory,
                                        @Param("processor") String processor,
                                        @Param("resolution") String resolution
    );

}
