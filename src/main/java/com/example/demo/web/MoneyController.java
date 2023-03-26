package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AmountModel;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class MoneyController {

    private UserService userService;

    public MoneyController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/deposit")
    public String getDeposit(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        getBalance(appUserDetails,model);
        return "deposit";
    }

    @PostMapping("/deposit")
    public String postDeposit(@Valid @ModelAttribute(name = "amountModel") AmountModel amountModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AppUserDetails appUserDetails){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("amountModel", amountModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.amountModel",
                            bindingResult);
            return "redirect:/deposit";
        }

        String username = appUserDetails.getUsername();
        this.userService.deposit(amountModel,username);



        return "redirect:/";
    }

    @GetMapping("/withdraw")
    public String getWithdraw(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        getBalance(appUserDetails,model);
        return "withdraw";
    }

    @PostMapping("/withdraw")
    public String postWithdraw(@Valid @ModelAttribute(name = "amountModel") AmountModel amountModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AppUserDetails appUserDetails){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("amountModel", amountModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.amountModel",
                            bindingResult);
            return "redirect:/withdraw";
        }

        String username = appUserDetails.getUsername();
        boolean isWithdrew = this.userService.withdraw(amountModel, username);
        if(!isWithdrew){
            redirectAttributes.addFlashAttribute("bad_credentials",true);
            return "redirect:/withdraw";
        }


        return "redirect:/";
    }


    @ModelAttribute(name = "amountModel")
    public AmountModel amountModel(){
        return new AmountModel();
    }

    private void getBalance(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        String username = appUserDetails.getUsername();
        UserModel byUsername = this.userService.findByUsername(username);
        model.addAttribute("balance", byUsername.getBalance());
    }
}
