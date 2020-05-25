package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.CreateProductDto;
import pl.kesco.myfarmer.model.dto.EditProductDto;
import pl.kesco.myfarmer.model.dto.ProductToBasketDto;
import pl.kesco.myfarmer.model.entity.BasketPosition;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.service.BasketService;
import pl.kesco.myfarmer.service.ImageService;
import pl.kesco.myfarmer.service.ProductService;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping("products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final BasketService basketService;
    private final ImageService imageService;


    @GetMapping
    public String showAllProducts(final ModelMap model,
                                  ProductToBasketDto productToBasketDto) {

        model.addAttribute("ordered", productToBasketDto);
        model.addAttribute("products", productService.getAllProducts());
        return "product/all-products";
    }

    @GetMapping("/{id}")
    public String showProduct(@PathVariable("id") Long productId,
                              final ModelMap model,
                              ProductToBasketDto productQnty) {

        boolean productOptional = productService.findById(productId).isEmpty();

        if (productOptional) {

            return "redirect:/products";
        }

        Product productToDisplay = productService.findById(productId).get();

        model.addAttribute("ordered", productQnty);
        model.addAttribute("product", productToDisplay);

        return "product/product";
    }

    @PostMapping("/buy/{id}")
    public ModelAndView addToBasket(@PathVariable("id") Long productId,
                                    @Valid @ModelAttribute("ordered") ProductToBasketDto productToBasketDto,
                                    final ModelMap model) {


        var productToOrder = productService.findById(productId)
                //TODO throw error
                .orElseThrow();

        final Long productToOrderQuantity = productToBasketDto.getQuantity();

        if (productToOrderQuantity > productToOrder.getQuantity() || productToOrderQuantity <= 0) {
            return new ModelAndView("redirect:/oops", model);
        }

        basketService.add(BasketPosition.builder()
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
                                      BindingResult bindingResult,
                                      @RequestParam("file") MultipartFile file,
                                      final ModelMap model) throws IOException, InterruptedException {

        if (bindingResult.hasErrors()) {
            model.addAttribute("product", productDto);
            return new ModelAndView("product/new-product", model);
        }

        final String imageUrl = imageService.uploadImage(file);

        productService.create(
                Product
                        .builder()
                        .name(productDto.getName())
                        .description(productDto.getDescription())
                        .price(productDto.getPrice())
                        .quantity(productDto.getQuantity())
                        .unit(productDto.getUnit())
                        .imageUrl(imageUrl)
                        .build()
        );

        return new ModelAndView("redirect:/", model);
    }

    @GetMapping("/edit/{id}")
    public String showProductToEdit(@PathVariable("id") Long productId,
                                    final ModelMap model,
                                    EditProductDto editProduct) {

        Optional<Product> productToEdit = productService.findById(productId);

        if (productToEdit.isEmpty()) {

            model.addAttribute("product", editProduct);

            return "redirect:/products/new";
        }

        productToEdit.ifPresent(
                product -> {
                    editProduct.setDescription(product.getDescription());
                    editProduct.setName(product.getName());
                    editProduct.setPrice(product.getPrice());
                    editProduct.setQuantity(product.getQuantity());
                    editProduct.setUnit(product.getUnit());
                }
        );

        model.addAttribute("product", editProduct);

        return "product/edit-product";
    }

    @PostMapping("/edit/{id}")
    public ModelAndView editProduct(@Valid @ModelAttribute("product") EditProductDto editProduct,
                                    @PathVariable("id") Long productId,
                                    final ModelMap model) {

        productService.update(productId, Product.builder()
                .name(editProduct.getName())
                .description(editProduct.getDescription())
                .unit(editProduct.getUnit())
                .quantity(editProduct.getQuantity())
                .price(editProduct.getPrice())
                .build());

        return new ModelAndView("redirect:/users/products", model);
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable("id") Long productId,
                                final ModelMap model) {

        productService.delete(productId);

        return "redirect:/users/products";

    }


}
