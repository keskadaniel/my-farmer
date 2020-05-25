package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.CreateProductDto;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.OrderService;

import javax.validation.Valid;

@Controller
@RequestMapping("orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ModelAndView createOrder() {

        orderService.create();

        return new ModelAndView("redirect:/");
    }

    @GetMapping
    public String showCompletedOrders(final ModelMap model) {

        model.addAttribute("orders",orderService.readAllCompletedOrders());

        return "order/all-orders";
    }


}
