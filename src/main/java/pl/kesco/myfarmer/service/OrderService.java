package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.persistence.OrderRepository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

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

    public Optional<Order> findById(Long orderId){

        return orderRepo.findById(orderId);
    }

    public List<Order> findLastOpenOrderOfLoggedUser(){

        var user = userService.getLoggedUser();

        return orderRepo.findAllByCustomerIdAndOrderedFalseOrderByDateDesc(user);
    }

}
