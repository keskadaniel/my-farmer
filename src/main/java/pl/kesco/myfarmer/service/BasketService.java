package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Basket;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.persistence.BasketRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketService {

    private final BasketRepository basketRepo;
    private final OrderService orderService;

    public Basket add(Basket basket) {

        var order = orderService.findLastOpenOrderOfLoggedUser().stream()
                .findFirst()
                .orElseGet(() -> orderService.create());

        //TODO use services instead of repo
        var newBasketPosition = basketRepo.save(basket.toBuilder()
                .order(order)
                .build());

        log.info("Product with id: {} added to basket! Order id: {}", basket.getProduct().getId(), order.getId());

        return newBasketPosition;
    }

    public List<Basket> readAllBasketPositions() {

        final Optional<Order> lastUserOrder = findLastUserOrder();

        return lastUserOrder.isPresent() ? basketRepo.findAllByOrder(lastUserOrder.get()) : new ArrayList<>();

    }

    public Double getTotalPrice() {

        final Optional<Order> lastUserOrder = findLastUserOrder();

        return lastUserOrder.isPresent() ? basketRepo.findAllByOrder(lastUserOrder.get()).stream()
                .map(p -> p.countPrice())
                .collect(Collectors.summingDouble(Double::doubleValue))
                : 0D;

    }

    private Optional<Order> findLastUserOrder() {
        return orderService.findLastOpenOrderOfLoggedUser().stream()
                .findFirst();
    }


}
