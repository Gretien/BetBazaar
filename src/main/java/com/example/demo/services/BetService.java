package com.example.demo.services;

import com.example.demo.domain.entities.Bet;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.BetCreatorModel;

import java.util.List;

public interface BetService {
    boolean createBet(BetCreatorModel betCreatorModel, String username);

    List<Bet> getBetsByUsername(String username);

    Bet findBetById(String id);

    String getProfit(UserModel userModel);
}
