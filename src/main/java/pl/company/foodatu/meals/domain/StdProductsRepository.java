package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

interface StdProductsRepository {

    StdProduct save(StdProduct stdProduct);

    Page<StdProduct> findAll(Pageable pageable);

    Optional<StdProduct> findById(String id);
}
