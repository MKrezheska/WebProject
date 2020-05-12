package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import com.example.demo.model.ProductDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface JpaProductDetailsRepository extends JpaRepository<ProductDetails, Long> {


    @Query(value = "select * from products_details c " +
            "WHERE  CONCAT('%',:memory,'%') LIKE" +
            "              CASE WHEN c.memory LIKE CONCAT('%Two%') THEN " +
            "                CONCAT('%Two%')" +
            "              WHEN c.memory ~ CONCAT('[^+]*',:memory,'[^+]*') THEN " +
            "                CONCAT('%',:memory,'%')" +
            "             END " +
            "and c.display like %:display% " +
            "and c.graphics_card like %:graphicsCard% " +
            "and c.internal_memory ~ CONCAT('^(RAM |RAM: ?|Меморија: )?',:internalMemory)" +
            "and c.processor like %:processor% " +
            "and c.resolution like %:resolution%"
            , nativeQuery=true)
    List<ProductDetails> searchProducts(@Param("display") String display,
                                        @Param("graphicsCard") String graphicsCard,
                                        @Param("internalMemory") String internalMemory,
                                        @Param("memory") String memory,
                                        @Param("processor") String processor,
                                        @Param("resolution") String resolution
    );

}
