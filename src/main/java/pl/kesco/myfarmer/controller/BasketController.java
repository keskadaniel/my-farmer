package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import pl.kesco.myfarmer.model.dto.EditBasketDto;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.OrderService;

import javax.validation.Valid;
import java.util.Map;

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


    @PostMapping("/edit/{id}")
    public ModelAndView editProduct(@Valid @ModelAttribute("basket") EditBasketDto editBasketDto,
                                    @PathVariable("id") Long basketPositionId,
                                    final ModelMap model,
                                    RedirectAttributes redirectAttributes) {

        if (requiredQntyExceedInStockQnty(basketPositionId, editBasketDto.getQuantity())) {
            redirectAttributes.addFlashAttribute("message", "Nie mamy tyle na stanie!");
            return new ModelAndView("redirect:/basket/edit/" + basketPositionId);
        }

        basketService.update(basketPositionId, editBasketDto.getQuantity());

        return new ModelAndView("redirect:/basket");
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long basketPositionId) {

        basketService.delete(basketPositionId);

        return "redirect:/basket";

    }


    private boolean requiredQntyExceedInStockQnty(Long basketPositionId, Long quantity) {

        if(quantity < 0){
            return true;
        }

        long exceedQuantity = basketService.readById(basketPositionId)
                .filter(basketPosition -> basketPosition.getProduct().getQuantity() < quantity)
                .stream()
                .count();

        return exceedQuantity > 0 ? true : false;
    }

}
