package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.persistence.ProductRepository;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {

    private final ProductRepository productRepo;
    private final UserService userService;

    public Product create(Product product){

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


}
