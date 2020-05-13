package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.BasketRepository;
import pl.kesco.myfarmer.persistence.OrderRepository;
import pl.kesco.myfarmer.service.mail.EmailService;

import javax.transaction.Transactional;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserService userService;
    private final BasketRepository basketRepo;
    private final ProductService productService;
    private final EmailService emailService;

    public Order create() {

        final var user = userService.getLoggedUser();

        var newOrder = orderRepo.save(
                Order
                        .builder()
                        .customerId(user)
                        .date(ZonedDateTime.now())
                        .ordered(false)
                        .build()
        );

        log.info("User with id: {} created new order with id: {}", user.getId(), newOrder.getId());

        return newOrder;
    }

    public Optional<Order> findById(Long orderId) {

        return orderRepo.findById(orderId);
    }

    public List<Order> findLastOpenOrderOfLoggedUser() {

        var user = userService.getLoggedUser();

        return orderRepo.findAllByCustomerIdAndOrderedFalseOrderByDateDesc(user);
    }

    @Transactional
    public void completeOrder() {

        final var user = userService.getLoggedUser();

        Optional<Order> userOrder = orderRepo.findAllByCustomerIdAndOrderedFalseOrderByDateDesc(user).stream()
                .findFirst();

        userOrder.ifPresent(order -> orderRepo.save(order
                .toBuilder()
                .ordered(true)
                .build())
        );

        //TODO validate if products are still in stock

        userOrder.ifPresent(order -> {
            basketRepo.findAllByOrder(order).stream()
                    .forEach(basket ->
                            productService.updateQuantity(basket.getProduct(), basket.getQuantity()));
        });

        sendEmailToCustomer(user, userOrder);

    }

    private void sendEmailToCustomer(User user, Optional<Order> userOrder) {

        final String content = "Twoje zamówienie o numerze "+ userOrder.get().getId() + " zostało wysłane do Rolnika!";

        emailService.sendMessage(user.getEmail(), "Realizacja zamówienia", content);
    }

}
