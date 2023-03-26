package com.example.demo.web;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.domain.models.binding.BetCreatorModel;
import com.example.demo.services.BetService;
import com.example.demo.services.MatchService;
import com.example.demo.services.UserService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/matches")
public class MatchController {
    private MatchService matchService;
    private UserService userService;
    private AddBetModel addBetModel;
    private List<AddBetModel> addBetModels;
    private BetService betService;

    public MatchController(MatchService matchService, UserService userService, AddBetModel addBetModel, BetService betService) {
        this.matchService = matchService;
        this.userService = userService;
        this.addBetModel = addBetModel;
        this.betService = betService;
        this.addBetModels = new ArrayList<>();
    }

    @GetMapping
    public String getMatches(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model){
        if(addBetModel.getName()!=null) {
            if(addBetModels.stream().noneMatch(e -> e.getName().substring(0,15).equals(addBetModel.getName().substring(0,15)))) {
                addBetModels.add(this.addBetModel);
            }
        }
        model.addAttribute("bets", addBetModels);
        String totalOdds = getTotalOdds();
        model.addAttribute("totalOdds",totalOdds);
        getBalance(appUserDetails, model);
        List<Match> matches = this.matchService.initMatches();
        model.addAttribute("matches",matches);
        return "matches";
    }

    @PostMapping
    public String postMatches(@ModelAttribute(name = "betCreator")BetCreatorModel betCreatorModel,
                              @AuthenticationPrincipal AppUserDetails appUserDetails, RedirectAttributes redirectAttributes){
        if(betCreatorModel.getPrice()==null){
            redirectAttributes.addFlashAttribute("nullValue",true);
            return "redirect:/matches";
        }

        if(this.addBetModels.size()==0){
            redirectAttributes.addFlashAttribute("noMatches",true);
            return "redirect:/matches";
        }
        betCreatorModel.setMatches(this.addBetModels);
        betCreatorModel.setTotalOdds(mapOddsToDouble());
        String username = appUserDetails.getUsername();
        boolean isCreated = this.betService.createBet(betCreatorModel, username);
        if(!isCreated){
            redirectAttributes.addFlashAttribute("bad_credentials",true);
            return "redirect:/matches";
        }
        this.addBetModels = new ArrayList<>();
        this.addBetModel.setName(null);
        return "redirect:/bets";
    }

    private Double mapOddsToDouble() {
        String totalOdds = getTotalOdds().replace(",",".");
        return Double.valueOf(totalOdds);
    }

    @GetMapping("/bet/remove/{name}")
    public String getBetRemoveMatch(@PathVariable String name){
        this.addBetModels.removeIf(betModel -> betModel.getName().equals(name));
        this.addBetModel.setName(null);
        return "redirect:/matches";
    }


    @GetMapping("/bet/add/{odd}")
    public String getBetAddMatch(@PathVariable Double odd){
        this.addBetModel = this.matchService.getMatchInBet(odd);
        return "redirect:/matches";
    }

    @ModelAttribute(name = "betCreator")
    public BetCreatorModel betCreatorModel(){
        return new BetCreatorModel();
    }

    private void getBalance(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        String username = appUserDetails.getUsername();
        UserModel byUsername = this.userService.findByUsername(username);
        model.addAttribute("balance", byUsername.getBalance());
    }

    private String getTotalOdds() {
        List<Double> odds = addBetModels.stream().map(AddBetModel::getOdd).collect(Collectors.toList());
        double totalOdds = 1.0;
        for (Double odd : odds) {
            totalOdds*=odd;
        }
        return String.format("%.2f", totalOdds);
    }


}
