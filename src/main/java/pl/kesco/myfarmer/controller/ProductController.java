package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.CreateProductDto;
import pl.kesco.myfarmer.model.dto.ProductToBasket;
import pl.kesco.myfarmer.model.entity.Basket;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.OrderService;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.ProductService;
import pl.kesco.myfarmer.service.UserService;

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


    @PostMapping("/buy/{id}")
    public ModelAndView addToBasket(@PathVariable("id") Long productId,
            @Valid @ModelAttribute("ordered") ProductToBasket productToBasket,
                                    final ModelMap model) {

        var order = orderService.findLastOrderOfLoggedUser().stream()
                .findFirst()
                .orElseGet(() -> orderService.create());

        var product = productService.findById(productId)
                //TODO throw error
                .orElseThrow();

        ordProdService.add(Basket
                .builder()
                .order(order)
                .product(product)
                .quantity(1L)
                .build()
        );

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
