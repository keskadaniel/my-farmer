package pl.kesco.myfarmer.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("login")
    public String loginUser(){

        return "login";
    }

    @GetMapping("oops")
    public String nonUniqueEmail(){

        return "user/error-page";
    }

}
