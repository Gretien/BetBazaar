package com.example.demo.services;

import com.example.demo.domain.entities.User;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AmountModel;
import com.example.demo.domain.models.binding.UserRegister;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;
import java.util.function.Consumer;

public interface UserService {
    void register(UserRegister userRegister, Consumer<Authentication> successfulLoginProcessor);

    void deposit(AmountModel depositModel, String username);

    UserModel findByUsername(String username);

    boolean withdraw(AmountModel depositModel, String username);

    void subtractMoneyForBet(User user, BigDecimal price);

    void addWinningMoney(User user, double winningAmount);

    List<User> findAll();

    void dailyBonus(User user);
}
