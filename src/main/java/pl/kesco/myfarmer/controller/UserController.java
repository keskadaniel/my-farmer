package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.*;
import pl.kesco.myfarmer.model.entity.Role;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.service.ProductService;
import pl.kesco.myfarmer.service.RoleService;
import pl.kesco.myfarmer.service.UserService;
import pl.kesco.myfarmer.service.UtilService;

import javax.validation.Valid;
import javax.validation.constraints.Size;
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

    public static final String ADD_ROLE = "add_role";
    public static final String REMOVE_ROLE = "remove_role";

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showAllUsers(final ModelMap model) {

        model.addAttribute("users", userService.readAllUsers());

        return "user/all-users";
    }

    @GetMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String showUserToEdit(@PathVariable("id") Long userId,
                                 final ModelMap model,
                                 EnableUserDto enableUserDto,
                                 EditRoleDto editRoleDto) {

        Optional<User> optionaluser = userService.readById(userId);

        if (optionaluser.isEmpty()) {

            return "redirect:/users/new";
        }

        optionaluser.ifPresent(
                user -> {
                    enableUserDto.setEnabled(user.isEnabled());
                }
        );

        model.addAttribute("userEdit", enableUserDto);
        model.addAttribute("user", optionaluser.get());
        model.addAttribute("userRole", editRoleDto);

        return "user/edit-user";
    }

    @PostMapping("/edit/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView editUser(@Valid @ModelAttribute("userEdit") EnableUserDto enableUserDto,
                                 @PathVariable("id") Long userId,
                                 final ModelMap model) {

        Optional<User> user = userService.readById(userId);

        user.ifPresent(user1 -> {
            userService.update(userId, user1.toBuilder()
                    .enabled(enableUserDto.isEnabled())
                    .build());
        });

        return new ModelAndView("redirect:/users", model);
    }

    @PostMapping("/addRole/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView addRole(@Valid @ModelAttribute("userRole") EditRoleDto editRoleDto,
                                @PathVariable("id") Long userId,
                                final ModelMap model) {

        editRole(editRoleDto, userId, ADD_ROLE);

        return new ModelAndView("redirect:/users/edit/" + userId, model);
    }

    @PostMapping("/removeRole/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ModelAndView removeRole(@Valid @ModelAttribute("userRole") EditRoleDto editRoleDto,
                                   @PathVariable("id") Long userId,
                                   final ModelMap model) {

        editRole(editRoleDto, userId, REMOVE_ROLE);

        return new ModelAndView("redirect:/users/edit/" + userId, model);
    }

    private void editRole(EditRoleDto editRoleDto, Long userId, String action) {
        Optional<User> user = userService.readById(userId);

        user.ifPresent(serToEdit -> {
            userService.update(userId, serToEdit.toBuilder()
                    .roles(updateRoles(editRoleDto.getRole(), serToEdit, action))
                    .build());
        });
    }

    @GetMapping("/delete/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String deleteUser(@PathVariable("id") Long userId) {

        userService.delete(userId);

        return "redirect:/users";

    }

    private Set<Role> updateRoles(String roleName, User user, String action) {

        Set<Role> userRoles = user.getRoles();

        if (action.equals(REMOVE_ROLE)) {
            userRoles.remove(getRole(roleName));
        } else if (action.equals(ADD_ROLE)) {
            userRoles.add(getRole(roleName));
        }

        return userRoles;
    }

    private Role getRole(String roleName) {
        return roleService.readByName(roleName);
    }

    @GetMapping("/my-account")
    public String userAccount(final ModelMap model) {

        model.addAttribute("user", userService.getLoggedUser());

        return "user/my-account";
    }

    @GetMapping("/my-account/edit")
    @PreAuthorize("isAuthenticated()")
    public String showUserDataToEdit(final ModelMap model,
                                     EditUserDataDto editUser) {

        var user = userService.getLoggedUser();

        editUser.setName(user.getName());
        editUser.setEmail(user.getEmail());
        editUser.setPhoneNumber(getPhoneNumber(user));

        model.addAttribute("user", editUser);

        return "user/edit-my-account";
    }

    private String getPhoneNumber(User user) {

        if (StringUtils.isEmpty(user.getPhoneNumber())) {
            return "No number";
        }

        return user.getPhoneNumber();
    }

    @PostMapping("/my-account/edit")
    public ModelAndView editMyAccount(@Valid @ModelAttribute("user") EditUserDataDto editUserDataDto,
                                      BindingResult bindingResult,
                                      final ModelMap model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", editUserDataDto);
            return new ModelAndView("user/edit-my-account", model);
        }

        var user = userService.getLoggedUser();
        String oldEmail = user.getEmail();

        boolean isEmailUnique = validateUniqueEmailForAccount(editUserDataDto.getEmail(), user);

        if (isEmailUnique == false) {
            return new ModelAndView("redirect:/oops", model);
        }

        userService.update(user.getId(), user.toBuilder()
                .name(editUserDataDto.getName())
                .email(editUserDataDto.getEmail())
                .phoneNumber(editUserDataDto.getPhoneNumber())
                .build());

        if(!oldEmail.equals(editUserDataDto.getEmail())){
            return new ModelAndView("redirect:/logout");
        }

        return new ModelAndView("redirect:/users/my-account", model);
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

    private boolean validateUniqueEmailForAccount(String email, User user) {
        return utilService.validateIsEmailUniqueForExistingUser(email, user);
    }


}
