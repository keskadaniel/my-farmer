package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.BasketPosition;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.persistence.BasketRepository;

import javax.swing.text.html.Option;
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

    public void add(BasketPosition basketPosition) {

        var order = orderService.findLastOpenOrderOfLoggedUser().stream()
                .findFirst()
                .orElseGet(() -> orderService.create());

        basketRepo.findAllByOrder(order).stream()
                .filter(b -> b.getProduct().equals(basketPosition.getProduct()))
                .findFirst()
                .ifPresentOrElse(
                        basketItem -> basketRepo.save(basketItem.toBuilder()
                                .quantity(basketItem.getQuantity() + basketPosition.getQuantity())
                                .build()),
                        () -> basketRepo.save(basketPosition.toBuilder()
                                .order(order)
                                .build())

                );

        log.info("Product with id: {} added to basket! Order id: {}", basketPosition.getProduct().getId(), order.getId());

    }

    public List<BasketPosition> readAllBasketPositions() {

        final Optional<Order> lastUserOrder = findLastUserOrder();

        return lastUserOrder.isPresent() ? basketRepo.findAllByOrder(lastUserOrder.get()) : new ArrayList<>();

    }

    public Double getTotalPrice() {

        final Optional<Order> lastUserOrder = findLastUserOrder();

        return lastUserOrder.isPresent() ? basketRepo.findAllByOrder(lastUserOrder.get()).stream()
                .map(p -> p.countProductPrice())
                .collect(Collectors.summingDouble(Double::doubleValue))
                : 0D;

    }

    public void update(Long id, Long quantity){

        basketRepo.findById(id)
                .map(basketPosition -> basketPosition.toBuilder()
                .quantity(quantity)
                .build())
                .ifPresent(basketRepo::save);

        log.info("Position no {} was updated in your basket! New Quantity: {}", id, quantity);
    }

    private Optional<Order> findLastUserOrder() {
        return orderService.findLastOpenOrderOfLoggedUser().stream()
                .findFirst();
    }

    public Optional<BasketPosition> readById(Long id){

        return basketRepo.findById(id);

    }

    public void delete(Long basketPositionId) {

        basketRepo.findById(basketPositionId)
                .ifPresent(basketPosition -> basketRepo.delete(basketPosition));

        log.info("Position no {} was deleted from your basket!", basketPositionId);
    }
}
