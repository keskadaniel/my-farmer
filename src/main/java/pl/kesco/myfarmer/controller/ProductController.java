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
import pl.kesco.myfarmer.service.ProductService;
import pl.kesco.myfarmer.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final UserService userService;


    @GetMapping("/new")
    public String newProduct(CreateProductDto productDto
            , final ModelMap model){

        model.addAttribute("product", productDto);
        return "product/new-product";
    }


    @PostMapping
    public ModelAndView createProduct(@Valid @ModelAttribute("product") CreateProductDto productDto,
                                   final ModelMap model){

        productService.create(Product.builder()
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
