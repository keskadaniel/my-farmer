package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.kesco.myfarmer.model.entity.BasketPosition;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.Product;

import java.util.List;

public interface BasketRepository extends JpaRepository<BasketPosition, Long> {

    List<BasketPosition> findAllByOrder(Order order);
    List<BasketPosition> findAllByProduct(Product product);

}
