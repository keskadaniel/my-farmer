package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kesco.myfarmer.model.entity.Basket;

public interface BasketRepository extends JpaRepository<Basket, Long> {
}
