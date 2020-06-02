package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.AddUserDto;
import pl.kesco.myfarmer.model.dto.ProductToBasketDto;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.service.ProductService;
import pl.kesco.myfarmer.service.UserService;
import pl.kesco.myfarmer.service.UtilService;

import javax.validation.Valid;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UtilService utilService;
    private final ProductService productService;

    @GetMapping("/my-account")
    public String userAccount(final ModelMap model){

        model.addAttribute("user", userService.getLoggedUser());

        return "user/my-account";
    }

    @GetMapping("/new")
    public String addUser(AddUserDto addUserDto, final ModelMap model){
        //TODO model has params to rm
        model.addAttribute("user", addUserDto);
        return "user/new-user";
    }


    @PostMapping
    public ModelAndView addNewUser(@Valid @ModelAttribute("user") AddUserDto addUserDto,
                                   final ModelMap model){

        boolean isEmailUnique = validateUniqueEmailForAccount(addUserDto.getEmail());

        //TODO add alert for user
        if( isEmailUnique == false){
            return new ModelAndView("redirect:/oops", model);
        }

        userService.create(User.builder()
        .name(addUserDto.getName())
        .email(addUserDto.getEmail())
        .password(addUserDto.getPassword())
        .build());

        return new ModelAndView("redirect:/", model);
    }

    @GetMapping("/products")
    public String showAllProducts(final ModelMap model,
                                  ProductToBasketDto productToBasketDto) {

        model.addAttribute("products", productService.getAllUserProducts());
        return "user/my-products";
    }

    private boolean validateUniqueEmailForAccount(String email) {
        return utilService.validateIsEmailUnique(email);
    }


}
