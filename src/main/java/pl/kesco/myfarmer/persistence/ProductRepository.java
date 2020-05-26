package pl.kesco.myfarmer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.model.entity.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAllByOrderByUserIdAsc();
    List<Product> findAllByDeletedIsFalseOrderByUserIdAsc();
    Optional<Product> findByIdAndDeletedIsFalse(Long id);
    Optional<Product> findById(Long id);
    List<Product> findAllByUserIdOrderByCreateDate(User user);
    List<Product> findAllByUserIdAndDeletedIsFalseOrderByCreateDate(User user);
}
