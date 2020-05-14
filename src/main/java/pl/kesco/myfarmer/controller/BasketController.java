package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.apache.maven.wagon.ResourceDoesNotExistException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.ProductToBasketDto;
import pl.kesco.myfarmer.model.entity.Order;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.OrderService;

@Controller
@RequestMapping("basket")
@RequiredArgsConstructor
public class BasketController {

    private final BasketService basketService;
    private final OrderService orderService;

    @GetMapping
    public String showUserBasket(final ModelMap model) {

        model.addAttribute("basketProducts", basketService.readAllBasketPositions());
        model.addAttribute("totalPrice", basketService.getTotalPrice());
        return "user/basket";
    }

    @PostMapping
    public ModelAndView completeOrder(){

        try {
            orderService.completeOrder();
        } catch (ResourceDoesNotExistException e) {
            e.printStackTrace();
        }

        return new ModelAndView("redirect:/basket");
    }



}
