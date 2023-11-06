package pl.company.foodatu.meals.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
interface SpringStdProductsRepository extends CrudRepository<StdProduct, UUID>, StdProductsRepository {
}
