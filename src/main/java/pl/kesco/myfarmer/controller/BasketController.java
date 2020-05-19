package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kesco.myfarmer.model.dto.EditBasketDto;
import pl.kesco.myfarmer.model.dto.EditProductDto;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.OrderService;

import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("basket")
@RequiredArgsConstructor
@Slf4j
public class BasketController {

    private final BasketService basketService;
    private final OrderService orderService;

    @GetMapping
    public String showUserBasket(final ModelMap model) {

        model.addAttribute("basketProducts", basketService.readAllBasketPositions());
        model.addAttribute("totalPrice", basketService.getTotalPrice());
        model.addAttribute("status", true);


        return "basket/basket";
    }

    @PostMapping
    public ModelAndView completeOrder(RedirectAttributes redirectAttributes) {

        Map<String, Object> orderedProducts = orderService.completeOrder();

        if (orderedProducts.get("ordered").equals(false)) {
            redirectAttributes.addFlashAttribute("message", "Brakuje produktów!");
            redirectAttributes.addFlashAttribute("absent", orderedProducts.get("products"));
            new ModelAndView("redirect:/basket");
        }

        return new ModelAndView("redirect:/basket");
    }

    @GetMapping("/edit/{id}")
    public String showBasketToEdit(@PathVariable("id") Long productId,
                                   final ModelMap model,
                                   EditBasketDto editBasketDto) {


        model.addAttribute("basketProducts", basketService.readAllBasketPositions());
        model.addAttribute("id", productId);

        basketService.readById(productId).ifPresent(
                basketPosition -> {
                    editBasketDto.setQuantity(basketPosition.getQuantity());
                    model.addAttribute("basket", editBasketDto);
                }
        );

        return "basket/edit-basket";
    }


}
