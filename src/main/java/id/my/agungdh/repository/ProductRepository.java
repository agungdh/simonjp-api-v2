package id.my.agungdh.repository;

import id.my.agungdh.entity.Product;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@ApplicationScoped
public class ProductRepository {

    private final Map<Long, Product> store = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public List<Product> findAll() {
        return store.values().stream()
                .sorted((a, b) -> Long.compare(a.getId(), b.getId()))
                .toList();
    }

    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(store.get(id));
    }

    public Product save(Product product) {
        long id = sequence.incrementAndGet();
        product.setId(id);
        store.put(id, product);
        return product;
    }

    public boolean update(Product product) {
        return store.computeIfPresent(product.getId(), (id, existing) -> product) != null;
    }

    public boolean delete(Long id) {
        return store.remove(id) != null;
    }
}
