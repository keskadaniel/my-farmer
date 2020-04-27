package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Basket;
import pl.kesco.myfarmer.persistence.BasketRepository;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketService {

    private final BasketRepository basketRepo;

    public Basket add(Basket basket){

        return basketRepo.save(basket);

    }

}
