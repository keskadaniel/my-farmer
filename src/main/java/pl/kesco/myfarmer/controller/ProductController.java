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
import pl.kesco.myfarmer.model.dto.ProductToBasket;
import pl.kesco.myfarmer.model.entity.Basket;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.OrderService;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BasketService ordProdService;
    private final OrderService orderService;


    @GetMapping
    public String showAllProducts(final ModelMap model,
                                  ProductToBasket productToBasket) {
        productToBasket.setQuantity(21L);
        model.addAttribute("ordered", productToBasket);
        model.addAttribute("products", productService.getAllProducts());
        return "product/products";
    }


    @PostMapping("/buy")
    public ModelAndView addToBasket(@Valid @ModelAttribute("ordered") ProductToBasket productToBasket,
                                    final ModelMap model) {

        var order = orderService.create();
        var basket =  Basket
                .builder()
                .id(32L)
                .order(order)
                .quantity(1L)
                .build();

        ordProdService.add(basket);

        return new ModelAndView("redirect:/products", model);
    }


    @GetMapping("/new")
    public String newProduct(CreateProductDto productDto
            , final ModelMap model) {

        model.addAttribute("product", productDto);
        return "product/new-product";
    }


    @PostMapping
    public ModelAndView createProduct(@Valid @ModelAttribute("product") CreateProductDto productDto,
                                      final ModelMap model) {

        productService.create(
                Product
                        .builder()
                        .name(productDto.getName())
                        .description(productDto.getDescription())
                        .price(productDto.getPrice())
                        .quantity(productDto.getQuantity())
                        .unit(productDto.getUnit())
                        .build()
        );

        return new ModelAndView("redirect:/", model);
    }


}
