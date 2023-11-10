package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

interface StdProductsRepository {

    StdProduct save(StdProduct stdProduct);

    Page<StdProduct> findAll(Pageable pageable);

    Optional<StdProduct> findById(UUID id);
}
