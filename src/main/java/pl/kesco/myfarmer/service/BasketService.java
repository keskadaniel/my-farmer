package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.BasketPosition;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.BasketRepository;
import pl.kesco.myfarmer.service.mail.EmailService;

import javax.transaction.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BasketService {

    private final BasketRepository basketRepo;
    private final OrderService orderService;
    private final ProductService productService;
    private final UserService userService;
    private final EmailService emailService;

    public void add(BasketPosition basketPosition) {

        var seller = basketPosition.getProduct().getUserId();

        var order = orderService.create(seller);

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

    @Transactional
    public Map<String, Object> buyProducts() {

        Map<String, Object> result = new HashMap<>();
        final String key = "ordered";

        List<BasketPosition> basketPositions = new ArrayList<>();
        final var user = userService.getLoggedUser();

        //ToDo take seller issue
        Optional<Order> userOrder = orderService.readUserOrder().stream().findFirst();


        if (userOrder.isPresent()) {
            basketPositions = findAllBasketPositions(userOrder.get());
        }

        List<BasketPosition> productsOutOfStock = validateProductsOutOfStock(basketPositions);

        boolean allProductsAvailable = productsOutOfStock.isEmpty();

        if (allProductsAvailable) {

            userOrder.ifPresent(order -> orderService.update(order
                    .toBuilder()
                    .ordered(true)
                    .total(getTotalPrice())
                    .completed(false)
                    .build())
            );

            basketPositions
                    .forEach(basket ->
                            productService.removeQuantity(basket.getProduct(), basket.getQuantity()));

            sendEmailToCustomer(user, userOrder);
            //assuming we have one Producer
            sendEmailToProducer(user, basketPositions, userOrder);
            log.info("Order No {} was successfully completed and sent to Producer!", userOrder.get().getId());
        } else {
            log.warn("Some products are out of stock!");
            result.put(key, false);
            result.put("products", productsOutOfStock);

            return result;
        }
        ;

        result.put(key, true);

        return result;
    }

    public List<BasketPosition> readAllBasketPositions() {

        final Optional<Order> lastUserOrder = findLastUserOrder();

        return lastUserOrder.isPresent() ? basketRepo.findAllByOrder(lastUserOrder.get()) : new ArrayList<>();

    }

    public List<BasketPosition> readAllBasketPositionsByOrder(Order order){

        return basketRepo.findAllByOrder(order);
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
        return orderService.readUserOrder().stream()
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

    private void sendEmailToCustomer(User user, Optional<Order> userOrder) {

        final String content = "Twoje zamówienie o numerze " + userOrder.get().getId() + " zostało wysłane do Rolnika!";

        emailService.sendMessage(user.getEmail(), "Realizacja zamówienia", content);
    }

    private void sendEmailToProducer(User user, List<BasketPosition> basketPositions, Optional<Order> userOrder) {
        final User producer = basketPositions.get(0).getProduct().getUserId();

        final String content = "Użytkownik " + user.getName() + " zamówił u Ciebie następujące produkty: "
                + basketPositions.toString();

        emailService.sendMessage(producer.getEmail(), "Nowe zamówienie!", content);
    }

    private List<BasketPosition> findAllBasketPositions(Order userOrder) {

        List<BasketPosition> basketPositions = new ArrayList<>();

        List<BasketPosition> allByOrder = readAllBasketPositionsByOrder(userOrder);

        if (allByOrder.size() > 0) {
            basketPositions = allByOrder;
        }

        return basketPositions;
    }

    public List<BasketPosition> validateProductsOutOfStock(List<BasketPosition> basketPositions) {

        return basketPositions.stream()
                .filter(basketPosition -> basketPosition.getProduct().getQuantity() < basketPosition.getQuantity())
                .collect(Collectors.toList());
    }

}
