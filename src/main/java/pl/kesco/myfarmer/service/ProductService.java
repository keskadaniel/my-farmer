package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.persistence.ProductRepository;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepo;

    public Product create(Product product){

        var newProduct = productRepo.save(
                product
                        .toBuilder()
                        .deleted(false)
                        .createDate(ZonedDateTime.now())
                .build());

        return newProduct;
    }


}
