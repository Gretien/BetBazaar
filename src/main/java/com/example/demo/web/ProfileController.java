package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.services.BetService;
import com.example.demo.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
public class ProfileController {
    private UserService userService;
    private BetService betService;

    public ProfileController(UserService userService, BetService betService) {
        this.userService = userService;
        this.betService = betService;
    }

    @GetMapping
    public String getProfile(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        UserModel userModel = this.userService.findByUsername(appUserDetails.getUsername());
        model.addAttribute("user",userModel);
        model.addAttribute("balance",userModel.getBalance());
        String profit = this.betService.getProfit(userModel);
        model.addAttribute("profit",profit);
        return "profile";
    }
}
