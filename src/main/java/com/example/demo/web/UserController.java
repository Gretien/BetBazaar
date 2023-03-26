package com.example.demo.web;

import com.example.demo.domain.models.binding.UserRegister;
import com.example.demo.services.UserService;
import com.example.demo.validations.UserRegisterValidator;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/users")
public class UserController {
    private UserService userService;
    private SecurityContextRepository securityContextRepository;
    private UserRegisterValidator userRegisterValidator;

    @Autowired
    public UserController(UserService userService, SecurityContextRepository securityContextRepository, UserRegisterValidator userRegisterValidator) {
        this.userService = userService;
        this.securityContextRepository = securityContextRepository;
        this.userRegisterValidator = userRegisterValidator;
    }

    @GetMapping("/register")
    public String getRegister(){
        return "auth-register";
    }

    @PostMapping("/register")
    public String postRegister(@Valid @ModelAttribute(name = "userRegister") UserRegister userRegister,
                               BindingResult bindingResult,
                               RedirectAttributes redirectAttributes, HttpServletRequest request,
                               HttpServletResponse response){
        this.userRegisterValidator.validate(userRegister, bindingResult);

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("userRegister", userRegister)
                    .addFlashAttribute("org.springframework.validation.BindingResult.userRegister",
                            bindingResult);
            return "redirect:/users/register";
        }
        this.userService.register(userRegister,successfulAuth -> {
            SecurityContextHolderStrategy strategy = SecurityContextHolder.getContextHolderStrategy();

            SecurityContext context = strategy.createEmptyContext();
            context.setAuthentication(successfulAuth);

            strategy.setContext(context);

            securityContextRepository.saveContext(context, request, response);
        });
        return "redirect:/";
    }

    @GetMapping("/login")
    public String getLogin(){
        return "auth-login";
    }

    @PostMapping("/login-error")
    public String onFailedLogin(
            @ModelAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY) String username,
            RedirectAttributes redirectAttributes) {

        redirectAttributes.addFlashAttribute(UsernamePasswordAuthenticationFilter.SPRING_SECURITY_FORM_USERNAME_KEY, username);
        redirectAttributes.addFlashAttribute("bad_credentials", true);

        return "redirect:/users/login";
    }



    @ModelAttribute(name = "userRegister")
    public UserRegister userRegister(){
        return new UserRegister();
    }

}
