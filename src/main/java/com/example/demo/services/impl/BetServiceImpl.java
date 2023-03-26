package com.example.demo.services.impl;

import com.example.demo.domain.entities.Bet;
import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Result;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.domain.models.binding.BetCreatorModel;
import com.example.demo.repositories.BetRepository;
import com.example.demo.services.BetService;
import com.example.demo.services.ResultService;
import com.example.demo.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class BetServiceImpl implements BetService {
    private BetRepository betRepository;
    private UserService userService;
    private ModelMapper modelMapper;
    private ResultService resultService;

    public BetServiceImpl(BetRepository betRepository, UserService userService, ModelMapper modelMapper, ResultService resultService) {
        this.betRepository = betRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.resultService = resultService;
    }

    @Override
    public boolean createBet(BetCreatorModel betCreatorModel, String username) {
        UserModel userModel = this.userService.findByUsername(username);
        if((userModel.getBalance().subtract(betCreatorModel.getPrice())).doubleValue()<0||betCreatorModel.getPrice().intValue()==0){
            return false;
        }
        User user = this.modelMapper.map(userModel, User.class);
        this.userService.subtractMoneyForBet(user,betCreatorModel.getPrice());
        List<Match> matches = this.resultService.getResults(betCreatorModel.getMatches());
        boolean isWinning = isBetWinning(matches, betCreatorModel.getMatches());
        double winningAmount = betCreatorModel.getPrice().doubleValue() * betCreatorModel.getTotalOdds();
        if(isWinning){
            this.userService.addWinningMoney(user,winningAmount);
        }else {
            winningAmount = 0.0;
        }
        Integer numberOfBet = this.betRepository.findByUserUsername(username).orElseThrow(NoSuchElementException::new).size() + 1;
        Bet bet = new Bet(matches,betCreatorModel.getTotalOdds(),betCreatorModel.getPrice(),
                BigDecimal.valueOf(winningAmount),isWinning,numberOfBet,user);
        this.betRepository.saveAndFlush(bet);
        return true;
    }

    @Override
    public List<Bet> getBetsByUsername(String username) {
        List<Bet> bets = this.betRepository.findByUserUsername(username).orElse(new ArrayList<>());
        return bets;
    }

    @Override
    public Bet findBetById(String id) {
        Bet bet = this.betRepository.findById(id).orElseThrow(NoSuchElementException::new);
        return bet;
    }

    @Override
    public String getProfit(UserModel userModel) {
        List<Bet> bets = this.betRepository.findByUserUsername(userModel.getUsername()).orElseThrow(NoSuchElementException::new);
        double betMoney = bets.stream().mapToDouble(b -> b.getPrice().doubleValue()).sum();
        double winnings = bets.stream().mapToDouble(b -> b.getWinningMoney().doubleValue()).sum();
        return String.format("%.2f", winnings-betMoney);
    }

    private boolean isBetWinning(List<Match> matches, List<AddBetModel> matchesWithPredict) {
        for (int i = 0; i < matches.size(); i++) {
            String winner = matches.get(i).getResult().get(0).getWinner();
            String prediction = matchesWithPredict.get(i).getName().split(": ")[1];
            if(!winner.equals(prediction)){
                return false;
            }
        }
        return true;
    }
}
