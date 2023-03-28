package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class HomeController {
    private UserService userService;

    public HomeController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getHome(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        if(appUserDetails!=null){
            String username = appUserDetails.getUsername();
            UserModel byUsername = this.userService.findByUsername(username);
            model.addAttribute("balance",byUsername.getBalance());
        }
        return "index";
    }


}
