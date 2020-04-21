package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kesco.myfarmer.model.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}
