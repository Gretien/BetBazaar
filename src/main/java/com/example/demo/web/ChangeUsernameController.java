package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.services.UserService;
import com.example.demo.services.userDetails.ApplicationUserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequestMapping("/change/username")
public class ChangeUsernameController {
    private UserService userService;
    private ApplicationUserDetailsService applicationUserDetailsService;

    public ChangeUsernameController(UserService userService, ApplicationUserDetailsService applicationUserDetailsService) {
        this.userService = userService;
        this.applicationUserDetailsService = applicationUserDetailsService;
    }

    @GetMapping
    public String getChangeUsername(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        getBalance(appUserDetails, model);
        return "change-username";
    }

    @PostMapping
    public String postChangeUsername(@AuthenticationPrincipal AppUserDetails appUserDetails,
                                     @RequestParam String username, RedirectAttributes redirectAttributes){
        UserModel userToChange = this.userService.findByUsername(appUserDetails.getUsername());
        boolean isChanged = this.userService.changeUsername(username,userToChange);
        if(isChanged){
            return "redirect:/";
        }else {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:/change/username";
        }

    }

    private void getBalance(AppUserDetails appUserDetails, Model model) {
        String username = appUserDetails.getUsername();
        UserModel byUsername = this.userService.findByUsername(username);
        model.addAttribute("balance", byUsername.getBalance());
    }
}
