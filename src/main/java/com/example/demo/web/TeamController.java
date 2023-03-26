package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.TeamAddModel;
import com.example.demo.services.MatchService;
import com.example.demo.services.TeamService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/teams/add")
public class TeamController {
    private TeamService teamService;
    private UserService userService;
    private MatchService matchService;

    public TeamController(TeamService teamService, UserService userService, MatchService matchService) {
        this.teamService = teamService;
        this.userService = userService;
        this.matchService = matchService;
    }

    @GetMapping
    public String getTeamsAdd(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        String username = appUserDetails.getUsername();
        UserModel byUsername = this.userService.findByUsername(username);
        model.addAttribute("balance", byUsername.getBalance());

        return "team-add";
    }

    @PostMapping
    public String postTeamsAdd(@Valid
                               @ModelAttribute(name = "teamAddModel")
                                       TeamAddModel teamAddModel,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("teamAddModel", teamAddModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.teamAddModel",
                            bindingResult);
            return "redirect:/teams/add";
        }

        boolean areAdded = this.teamService.addTeams(teamAddModel);

        if (areAdded) {
            this.matchService.addMatch(teamAddModel.getFirstName(),teamAddModel.getSecondName());
            return "redirect:/";
        } else {
            redirectAttributes.addFlashAttribute("bad_credentials", true);
            return "redirect:/teams/add";
        }
    }

    @ModelAttribute(name = "teamAddModel")
    public TeamAddModel teamAddModel() {
        return new TeamAddModel();
    }
}
