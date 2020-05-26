package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.CreateProductDto;
import pl.kesco.myfarmer.model.dto.ProductToBasketDto;
import pl.kesco.myfarmer.model.entity.BasketPosition;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.OrderService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private final BasketService basketService;

    @PostMapping
    public ModelAndView createOrder() {

        return new ModelAndView("redirect:/");
    }

    @GetMapping
    public String showCompletedOrders(final ModelMap model) {

        model.addAttribute("orders",orderService.readAllCompletedOrders());

        return "order/all-orders";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long orderId,
                              final ModelMap model) {

        Optional<Order> optionalOrder = orderService.findById(orderId);

        if(optionalOrder.isEmpty()){
            return "redirect:/orders";
        }

        List<BasketPosition> basketPositions = basketService.readAllBasketPositionsByOrder(optionalOrder.get());

        model.addAttribute("basketProducts", basketPositions);
        model.addAttribute("totalPrice", optionalOrder.get().getTotal());

        return "order/order";
    }


    @GetMapping("/sold")
    public String showSellerOrders(@PathVariable("id") Long orderId,
                              final ModelMap model) {

        Optional<Order> optionalOrder = orderService.findById(orderId);

        if(optionalOrder.isEmpty()){
            return "redirect:/orders";
        }

        List<BasketPosition> basketPositions = basketService.readAllBasketPositionsByOrder(optionalOrder.get());

        model.addAttribute("basketProducts", basketPositions);
        model.addAttribute("totalPrice", optionalOrder.get().getTotal());

        return "order/order";
    }


}
