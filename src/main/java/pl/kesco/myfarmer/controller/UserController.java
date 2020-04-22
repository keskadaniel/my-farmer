package pl.kesco.myfarmer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import pl.kesco.myfarmer.model.dto.AddUserDto;
import pl.kesco.myfarmer.model.entity.User;
import pl.kesco.myfarmer.service.UserService;

import javax.validation.Valid;

@Controller
@RequestMapping("users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PasswordEncoder encoder;

    @GetMapping("/new")
    public String addUser(AddUserDto addUserDto, final ModelMap model){
        model.addAttribute("user", addUserDto);
        return "user/new-user";
    }


    @PostMapping
    public ModelAndView addNewUser(@Valid @ModelAttribute("user") AddUserDto addUserDto,
                                   final ModelMap model){

        userService.create(User.builder()
        .name(addUserDto.getName())
        .email(addUserDto.getEmail())
        .password(addUserDto.getPassword())
        .role(addUserDto.getRole())
        .build());

        return new ModelAndView("redirect:/", model);
    }


}
