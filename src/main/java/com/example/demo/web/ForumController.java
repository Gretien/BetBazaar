package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.PostAddModel;
import com.example.demo.domain.models.view.PostWithUsernameView;
import com.example.demo.services.PostService;
import com.example.demo.services.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/posts")
public class ForumController {
    private PostService postService;
    private UserService userService;

    public ForumController(PostService postService, UserService userService) {
        this.postService = postService;
        this.userService = userService;
    }

    @GetMapping("/add")
    public String getAddPost(@AuthenticationPrincipal AppUserDetails appUserDetails,Model model){
        getBalance(appUserDetails, model);
        return "post-add";
    }


    @PostMapping("/add")
    public String postAddPost(@Valid @ModelAttribute(name = "postAddModel") PostAddModel postAddModel,
                              BindingResult bindingResult,
                              RedirectAttributes redirectAttributes,
                              @AuthenticationPrincipal AppUserDetails appUserDetails){
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("postAddModel", postAddModel)
                    .addFlashAttribute("org.springframework.validation.BindingResult.postAddModel",
                            bindingResult);
            return "redirect:/posts/add";
        }
        String username = appUserDetails.getUsername();
        this.postService.addPost(postAddModel,username);

        return "redirect:/posts/all";
    }

    @GetMapping("/all")
    public String getAllPosts(@AuthenticationPrincipal AppUserDetails appUserDetails,Model model){
        List<PostWithUsernameView> posts = this.postService.findAll();
        model.addAttribute("posts",posts);
        String username = appUserDetails.getUsername();
        model.addAttribute("username",username);
        getBalance(appUserDetails, model);
        return "post-all";
    }

    @GetMapping("/remove/{id}")
    public String removePost(@PathVariable String id) {
        postService.removePostById(id);

        return "redirect:/posts/all";
    }

    @ModelAttribute(name = "postAddModel")
    public PostAddModel postAddModel(){
        return new PostAddModel();
    }

    private void getBalance(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        String username = appUserDetails.getUsername();
        UserModel byUsername = this.userService.findByUsername(username);
        model.addAttribute("balance", byUsername.getBalance());
    }
}
