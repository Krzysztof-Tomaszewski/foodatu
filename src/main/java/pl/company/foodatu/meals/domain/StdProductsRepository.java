package pl.company.foodatu.meals.domain;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

interface StdProductsRepository {

    StdProduct save(StdProduct stdProduct);

    List<StdProduct> findAll();

    Optional<StdProduct> findById(UUID id);
}
