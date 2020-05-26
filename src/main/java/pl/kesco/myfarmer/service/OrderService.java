package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.OrderRepository;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserService userService;

    public Order create(User seller) {

        final var user = userService.getLoggedUser();

        var newOrder = orderRepo.save(
                Order
                        .builder()
                        .customerId(user)
                        .sellerId(seller)
                        .date(ZonedDateTime.now())
                        .ordered(false)
                        .completed(false)
                        .build()
        );

        log.info("User with id: {} created new order with id: {}", user.getId(), newOrder.getId());

        return newOrder;
    }

    public void update(Order order){

        orderRepo.save(order);
    }

    public Optional<Order> findById(Long orderId) {

        return orderRepo.findById(orderId);
    }

    public List<Order> readUserOrder() {

        var user = userService.getLoggedUser();

        return orderRepo.findAllByCustomerIdAndOrderedFalseOrderByDateDesc(user);
    }



    public List<Order> readAllCompletedOrders() {

        List<Order> userOrders = new ArrayList<>();

        List<Order> completedOrders = orderRepo.findAllByCustomerIdAndOrderedTrueOrderByDateDesc(userService.getLoggedUser());

        if (completedOrders.size() > 0) {
            userOrders = completedOrders;
        }

        return userOrders;
    }

}
