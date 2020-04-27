package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.persistence.ProductRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
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


    public List<Product> getAllProducts() {

        return productRepo.findAllByOrderByUserIdAsc();
    }

    public Optional<Product> findById(Long id) {

        return productRepo.findById(id);
    }


    public void updateQuantity(Product product, Long quantity) {

        var productFromDatabase = productRepo.findById(product.getId());

        //TODO beware of changing stante of quantoty in db, check it
        productFromDatabase.ifPresent(prod -> {
                    Long updatedQuantity = prod.getQuantity() - quantity;

                    productRepo.save(Product
                            .builder()
                            .id(prod.getId())
                            .name(prod.getName())
                            .description(prod.getDescription())
                            .unit(prod.getUnit())
                            .userId(prod.getUserId())
                            .price(prod.getPrice())
                            .createDate(prod.getCreateDate())
                            .quantity(updatedQuantity)
                            .deleted(false)
                            .build());
                });
    }
}
