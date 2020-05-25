package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.persistence.ProductRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class ProductService {

    private final ProductRepository productRepo;
    private final UserService userService;

    public Product create(Product product) {

        var newProduct = productRepo.save(
                product
                        .toBuilder()
                        .userId(userService.getLoggedUser())
                        .deleted(false)
                        .createDate(ZonedDateTime.now())
                        .build());

        log.info("Created product: {}, with id: {}", newProduct.getName(), newProduct.getId());

        return newProduct;
    }

    @Transactional
    public void update(Long productId, Product product) {

        productRepo.findById(productId)
                .filter(product1 -> !product1.isDeleted())
                .map(productToUpdate -> productToUpdate.toBuilder()
                        .price(product.getPrice())
                        .unit(product.getUnit())
                        .name(product.getName())
                        .description(product.getDescription())
                        .quantity(product.getQuantity())
                        .build())
                .ifPresent(productRepo::save);
    }

    public void delete(Long id) {

        Optional<Product> optionalProduct = productRepo.findById(id);
        optionalProduct.ifPresent(product -> {
            if (product.isOrdered()) {
                productRepo.save(product.toBuilder()
                        .deleted(true)
                        .quantity(0L)
                        .name("Produkt usunięty")
                        .description("-")
                        .price(0D)
                        .build());

            } else {
                productRepo.delete(product);
            }

        });
    }


    public List<Product> getAllProducts() {

        return productRepo.findAllByOrderByUserIdAsc();
    }

    public List<Product> getAllUserProducts() {

        var user = userService.getLoggedUser();

        return productRepo.findAllByUserIdOrderByCreateDate(user);
    }

    public Optional<Product> findById(Long id) {

        return productRepo.findById(id);
    }


    public void updateQuantity(Product product, Long quantity) {

        var productFromDatabase = productRepo.findById(product.getId());

        productFromDatabase.ifPresent(prod -> {
            Long updatedQuantity = prod.getQuantity() - quantity;

            productRepo.save(prod
                    .toBuilder()
                    .quantity(updatedQuantity)
                    .ordered(true)
                    .build());

        });
    }
}
