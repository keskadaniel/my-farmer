package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.persistence.OrderRepository;

import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserService userService;

    public Order create() {

        var user = userService.getLoggedUser();

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


}
