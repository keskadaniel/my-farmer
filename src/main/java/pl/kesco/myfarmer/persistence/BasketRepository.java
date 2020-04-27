package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kesco.myfarmer.model.entity.Basket;
import pl.kesco.myfarmer.model.entity.Order;

import java.util.List;

public interface BasketRepository extends JpaRepository<Basket, Long> {

    List<Basket> findAllByOrder(Order order);

}
