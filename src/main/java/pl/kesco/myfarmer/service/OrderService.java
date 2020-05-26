package pl.kesco.myfarmer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.persistence.OrderRepository;
import pl.kesco.myfarmer.service.mail.EmailService;

import java.time.ZonedDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepo;
    private final UserService userService;
    private final EmailService emailService;

    @Transactional
    public Order create(User seller) {

        final var user = userService.getLoggedUser();

        var userOrder = readUserOrderFromSeller(user, seller).stream()
                .findFirst()
                .orElseGet(() -> orderRepo.save(
                        Order
                                .builder()
                                .customerId(user)
                                .sellerId(seller)
                                .date(ZonedDateTime.now())
                                .ordered(false)
                                .completed(false)
                                .build()));

        log.info("User with id: {} created new order with id: {}", user.getId(), userOrder.getId());

        return userOrder;
    }

    public void update(Order order) {

        orderRepo.save(order);
    }

    @Async
    public void sell(Long orderId) {

        orderRepo.findById(orderId).ifPresent(
                order -> {
                    orderRepo.save(order.toBuilder()
                            .completed(true)
                            .build());

                    sendEmailToCustomer(order);
                    log.info("Order no. {} was completed!", order.getId());
                });
    }

    private void sendEmailToCustomer(Order order) {
        String email = order.getCustomerId().getEmail();
        String subject = "Realizacja zamówienia";
        String content = new StringBuilder("Twoje zamówienie o numerze ")
                .append(order.getId())
                .append(" zostało zrealizowane!").toString();

        emailService.sendMessage(email, subject, content);
    }

    public Optional<Order> findById(Long orderId) {

        return orderRepo.findById(orderId);
    }

    public List<Order> readUserOrder() {

        var user = userService.getLoggedUser();

        return orderRepo.findAllByCustomerIdAndOrderedFalseOrderByDateDesc(user);
    }

    public List<Order> readOrdersFromSeller() {

        var user = userService.getLoggedUser();
        List<Order> sellerOrders = new ArrayList<>();

        var actualOrders = orderRepo.findAllBySellerIdAndOrderedTrueOrderByDateDesc(user);

        if (!actualOrders.isEmpty()) {
            sellerOrders = actualOrders;
        }

        return sellerOrders;
    }

    private List<Order> readUserOrderFromSeller(User customer, User seller) {

        return orderRepo.findAllByCustomerIdAndSellerIdAndOrderedFalseOrderByDateDesc(customer, seller);
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
