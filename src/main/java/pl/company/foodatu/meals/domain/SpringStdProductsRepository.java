package pl.company.foodatu.meals.domain;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
interface SpringStdProductsRepository extends CrudRepository<StdProduct, String>, StdProductsRepository {
}
