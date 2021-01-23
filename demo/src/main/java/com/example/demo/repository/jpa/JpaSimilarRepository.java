package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import com.example.demo.model.Similar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface JpaSimilarRepository extends JpaRepository<Similar, Long> {
    @Query(value = "select * from similarities " +
            "WHERE product_1 = :id", nativeQuery=true)
    List<Similar> findByFirstProductId(@Param("id") Long id);

    @Query(value = "UPDATE similarities SET most_similar = true" +
            "  WHERE product_1 = :id_1 and product_2 = :id_2", nativeQuery=true)
    Boolean updateMostSimilar(@Param("id") Long id_1, @Param("id") Long id_2);

}
