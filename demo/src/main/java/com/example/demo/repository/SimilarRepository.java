package com.example.demo.repository;

import com.example.demo.model.Product;
import com.example.demo.model.Similar;
import org.apache.xpath.operations.Bool;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SimilarRepository {
    Similar save(Similar similar);

    List<Similar> getAllSimilarities();

    List<Similar> getByProductId(Long productId);

    Boolean updateMostSimilar(Long id_1, Long id_2);

}
