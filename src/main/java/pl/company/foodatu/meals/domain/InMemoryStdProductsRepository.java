package pl.company.foodatu.meals.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

class InMemoryStdProductsRepository implements StdProductsRepository {

    Map<UUID, StdProduct> memory = new HashMap<>();

    @Override
    public StdProduct save(StdProduct stdProduct) {
        memory.put(stdProduct.getId(), stdProduct);
        return stdProduct;
    }

    @Override
    public Page<StdProduct> findAll(Pageable pageable) {
        List<StdProduct> stdProducts = memory.values().stream().toList();
        return new PageImpl<>(stdProducts, pageable, stdProducts.size());
    }

    @Override
    public Optional<StdProduct> findById(String id) {
        return Optional.ofNullable(memory.get(UUID.fromString(id)));
    }
}
