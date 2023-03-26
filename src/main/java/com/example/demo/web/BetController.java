package com.example.demo.web;

import com.example.demo.domain.entities.Bet;
import com.example.demo.domain.entities.Match;
import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.services.BetService;
import com.example.demo.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("/bets")
public class BetController {
    private UserService userService;
    private BetService betService;

    public BetController(UserService userService, BetService betService) {
        this.userService = userService;
        this.betService = betService;
    }

    @GetMapping
    public String getBets(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        getBalance(appUserDetails, model);
        List<Bet> bets = this.betService.getBetsByUsername(appUserDetails.getUsername());
        bets.sort(Comparator.comparing(Bet::getNumberOfBet).reversed());
        model.addAttribute("bets",bets);
        return "bets";
    }


    @GetMapping("/details/{id}")
    public String getBetDetails(@PathVariable String id,Model model,@AuthenticationPrincipal AppUserDetails appUserDetails){
        getBalance(appUserDetails, model);
        Bet bet = this.betService.findBetById(id);
        List<Match> matches = bet.getMatches();
        model.addAttribute("matches",matches);
        return "details";
    }

    @PostMapping("/details/{id}")
    public String postBetDetails(){
        return "redirect:/bets";
    }

    private void getBalance(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        String username = appUserDetails.getUsername();
        UserModel byUsername = this.userService.findByUsername(username);
        model.addAttribute("balance", byUsername.getBalance());
    }
}
