package com.example.demo.repository.jpa;

import com.example.demo.model.Product;
import com.example.demo.model.Similar;
import com.example.demo.repository.SimilarRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public class SimilarRepositoryImpl implements SimilarRepository {

    private final JpaSimilarRepository repository;

    public SimilarRepositoryImpl(JpaSimilarRepository repository) {
        this.repository = repository;
    }

    @Override
    public Similar save(Similar similar) {
        return this.repository.saveAndFlush(similar);
    }

    @Override
    public List<Similar> getAllSimilarities() {
        return this.repository.findAll();
    }

    @Override
    public List<Similar> getByProductId(Long productId) {
        return this.repository.findByFirstProductId(productId);
    }

    @Override
    public List<Similar> updateMostSimilar(Long id_1, Long id_2) {
        return this.repository.updateMostSimilar(id_1, id_2);
    }


}
