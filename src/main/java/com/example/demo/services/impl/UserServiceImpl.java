package com.example.demo.services.impl;

import com.example.demo.domain.entities.User;
import com.example.demo.domain.models.RoleModel;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AmountModel;
import com.example.demo.domain.models.binding.UserRegister;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.userDetails.ApplicationUserDetailsService;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;

@Service
public class UserServiceImpl implements UserService {
    private UserRepository userRepository;
    private ModelMapper modelMapper;
    private RoleService roleService;
    private PasswordEncoder passwordEncoder;
    private UserDetailsService userDetailsService;
    private ApplicationUserDetailsService applicationUserDetailsService;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper,
                           RoleService roleService, PasswordEncoder passwordEncoder,
                           UserDetailsService userDetailsService, ApplicationUserDetailsService applicationUserDetailsService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;

        this.applicationUserDetailsService = applicationUserDetailsService;
    }


    @PostConstruct
    public void initAdmin() {
        if(this.userRepository.count()==0){
            RoleModel roleModel = this.roleService.findByName();

            UserModel userModel = UserModel.builder()
                    .username("Admin")
                    .password(this.passwordEncoder.encode("12345"))
                    .firstName("Admin")
                    .lastName("Adminov")
                    .age(20)
                    .balance(BigDecimal.valueOf(1000))
                    .bets(new ArrayList<>())
                    .posts(new ArrayList<>())
                    .roles(List.of(roleModel))
                    .build();

            User user = this.modelMapper.map(userModel, User.class);
            this.userRepository.saveAndFlush(user);
        }
    }

    @Override
    public void register(UserRegister userRegister, Consumer<Authentication> successfulLoginProcessor){
        UserModel userModel = this.mapUser(userRegister);
        User user = this.modelMapper.map(userModel, User.class);
        this.userRepository.saveAndFlush(user);
        UserDetails userDetails = userDetailsService.loadUserByUsername(userRegister.getUsername());

        Authentication authentication = new UsernamePasswordAuthenticationToken(
                userDetails,
                userDetails.getPassword(),
                userDetails.getAuthorities()
        );

        successfulLoginProcessor.accept(authentication);
    }

    @Override
    public void deposit(AmountModel amountModel, String username) {
        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(amountModel.getAmount()));
        User user = this.userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        user.setBalance(user.getBalance().add(amount));
        this.userRepository.saveAndFlush(user);

    }

    @Override
    public UserModel findByUsername(String username) {
        User user = this.userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        return this.modelMapper.map(user,UserModel.class);
    }

    @Override
    public boolean withdraw(AmountModel amountModel, String username) {
        BigDecimal amount = BigDecimal.valueOf(Long.parseLong(amountModel.getAmount()));
        User user = this.userRepository.findByUsername(username).orElseThrow(NoSuchElementException::new);
        if(user.getBalance().subtract(amount).doubleValue()<0){
            return false;
        }
        user.setBalance(user.getBalance().subtract(amount));
        this.userRepository.saveAndFlush(user);
        return true;
    }

    @Override
    public void subtractMoneyForBet(User user, BigDecimal price) {
        user.setBalance(user.getBalance().subtract(price));
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public void addWinningMoney(User user, double winningAmount) {
        user.setBalance(user.getBalance().add(BigDecimal.valueOf(winningAmount)));
        this.userRepository.saveAndFlush(user);
    }

    @Override
    public List<User> findAll() {
        List<User> users = this.userRepository.findAll();
        return users;
    }

    @Override
    public void dailyBonus(User user) {
        user.setBalance(user.getBalance().add(BigDecimal.valueOf(10)));
        this.userRepository.saveAndFlush(user);
    }


    private UserModel mapUser(UserRegister userRegister){
        RoleModel roleModel = this.roleService.setToUser();

        return UserModel.builder()
                .username(userRegister.getUsername())
                .firstName(userRegister.getFirstName())
                .lastName(userRegister.getLastName())
                .age(userRegister.getAge())
                .password(this.passwordEncoder.encode(userRegister.getPassword()))
                .balance(BigDecimal.valueOf(0))
                .bets(new ArrayList<>())
                .posts(new ArrayList<>())
                .roles(List.of(roleModel))
                .build();
    }
}
