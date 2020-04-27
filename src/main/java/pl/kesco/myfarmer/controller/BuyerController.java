package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.kesco.myfarmer.service.BasketService;

@Controller
@RequestMapping("buy")
@RequiredArgsConstructor
public class BuyerController {

    private final BasketService ordProdService;


//    @PostMapping
//    public ModelAndView addToBasket(@Valid @ModelAttribute("ordered") ProductToBasket productToBasket,
//                                    final ModelMap model) {
//
//        ordProdService.addToBasket(
//                OrderedProducts
//                        .builder()
//                        .quantity(1L)
//                        .build()
//        );
//
//        return new ModelAndView("redirect:/products", model);
//    }


}
