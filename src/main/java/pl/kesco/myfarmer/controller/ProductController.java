package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.CreateProductDto;
import pl.kesco.myfarmer.model.dto.ProductToBasketDto;
import pl.kesco.myfarmer.model.entity.Basket;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.ProductService;

import javax.validation.Valid;

@Controller
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BasketService basketService;


    @GetMapping
    public String showAllProducts(final ModelMap model,
                                  ProductToBasketDto productToBasketDto) {

        model.addAttribute("ordered", productToBasketDto);
        model.addAttribute("products", productService.getAllProducts());
        return "product/products";
    }

    @PostMapping("/buy/{id}")
    public ModelAndView addToBasket(@PathVariable("id") Long productId,
            @Valid @ModelAttribute("ordered") ProductToBasketDto productToBasketDto,
                                    final ModelMap model) {


        var productToOrder = productService.findById(productId)
                //TODO throw error
                .orElseThrow();

        final Long productToOrderQuantity = productToBasketDto.getQuantity();

        if( productToOrderQuantity > productToOrder.getQuantity() || productToOrderQuantity <= 0){
            return new ModelAndView("redirect:/oops", model);
        }

        basketService.add(Basket
                .builder()
                .product(productToOrder)
                .quantity(productToOrderQuantity)
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
