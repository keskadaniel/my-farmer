package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.AddUserDto;
import pl.kesco.myfarmer.model.dto.EditProductDto;
import pl.kesco.myfarmer.model.dto.EditUserDto;
import pl.kesco.myfarmer.model.dto.ProductToBasketDto;
import pl.kesco.myfarmer.model.entity.Product;
import pl.kesco.myfarmer.model.entity.Role;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.service.ProductService;
import pl.kesco.myfarmer.service.RoleService;
import pl.kesco.myfarmer.service.UserService;
import pl.kesco.myfarmer.service.UtilService;

import javax.validation.Valid;
import java.util.Optional;
import java.util.Set;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UtilService utilService;
    private final ProductService productService;
    private final RoleService roleService;


    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAllusers(final ModelMap model) {

        model.addAttribute("users", userService.readAllUsers());

        return "user/all-users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUserToEdit(@PathVariable("id") Long userId,
                                 final ModelMap model,
                                 EditUserDto editUserDto) {

        Optional<User> optionaluser = userService.readById(userId);

        if (optionaluser.isEmpty()) {

            return "redirect:/users/new";
        }

        optionaluser.ifPresent(
                user -> {
                    editUserDto.setName(user.getName());
                    editUserDto.setRole(user.getRoles().iterator().next().getName());
                }
        );

        model.addAttribute("userEdit", editUserDto);
        model.addAttribute("user", optionaluser.get());

        return "user/edit-user";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editUser(@Valid @ModelAttribute("userEdit") EditUserDto editUserDto,
                                 @PathVariable("id") Long userId,
                                 final ModelMap model) {

        Optional<User> user = userService.readById(userId);

        user.ifPresent(user1 -> {
            userService.update(userId, user1.toBuilder()
                    .name(editUserDto.getName())
                    .roles(updateRoles(editUserDto.getRole(), user1))
                    .build());
        });

        return new ModelAndView("redirect:/users", model);
    }

    private Set<Role> updateRoles(String roleName, User user) {

        user.getRoles().add(getRole(roleName));

        return user.getRoles();
    }

    private Role getRole(String roleName) {
        return roleService.readByName(roleName);
    }

    @GetMapping("/my-account")
    public String userAccount(final ModelMap model) {

        model.addAttribute("user", userService.getLoggedUser());

        return "user/my-account";
    }

    @GetMapping("/new")
    public String addUser(AddUserDto addUserDto, final ModelMap model) {
        //TODO model has params to rm
        model.addAttribute("user", addUserDto);
        return "user/new-user";
    }


    @PostMapping
    public ModelAndView addNewUser(@Valid @ModelAttribute("user") AddUserDto addUserDto,
                                   final ModelMap model) {

        boolean isEmailUnique = validateUniqueEmailForAccount(addUserDto.getEmail());

        //TODO add alert for user
        if (isEmailUnique == false) {
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

        model.addAttribute("products", productService.readAllUserProducts());
        return "user/my-products";
    }

    private boolean validateUniqueEmailForAccount(String email) {
        return utilService.validateIsEmailUnique(email);
    }


}
