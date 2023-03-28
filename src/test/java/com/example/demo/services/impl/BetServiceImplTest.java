package com.example.demo.services.impl;

import com.example.demo.domain.entities.*;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.domain.models.binding.BetCreatorModel;
import com.example.demo.repositories.BetRepository;
import com.example.demo.services.ResultService;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BetServiceImplTest {
    @Mock
    private BetRepository betRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ResultService resultService;

    @InjectMocks
    private BetServiceImpl betService;

    @Test
    public void testCreateBetWithInvalidPrice() {
        BetCreatorModel betCreatorModel = new BetCreatorModel();
        betCreatorModel.setPrice(BigDecimal.valueOf(0));
        UserModel userModel = new UserModel();
        userModel.setBalance(BigDecimal.valueOf(100));
        when(userService.findByUsername(anyString())).thenReturn(userModel);

        boolean result = betService.createBet(betCreatorModel, "username");
        assertFalse(result);
    }

    @Test
    public void testCreateBetWithInsufficientFunds() {
        BetCreatorModel betCreatorModel = new BetCreatorModel();
        betCreatorModel.setPrice(BigDecimal.valueOf(50));
        UserModel userModel = new UserModel();
        userModel.setBalance(BigDecimal.valueOf(40));
        when(userService.findByUsername(anyString())).thenReturn(userModel);

        boolean result = betService.createBet(betCreatorModel, "username");

        assertFalse(result);
    }

    @Test
    public void testCreateBetWithValidBet() {

        BetCreatorModel betCreatorModel = new BetCreatorModel();
        betCreatorModel.setPrice(BigDecimal.valueOf(50));
        betCreatorModel.setTotalOdds(1.0);
        UserModel userModel = new UserModel();
        userModel.setBalance(BigDecimal.valueOf(100));
        List<AddBetModel> matchesWithPredict = new ArrayList<>();
        matchesWithPredict.add(new AddBetModel("Team A-Team B: Draw", 2.5));
        betCreatorModel.setMatches(matchesWithPredict);
        List<Match> matches = new ArrayList<>();
        Match match = new Match(new Team("Team A"), new Team("Team B"), 1.5, 2.5, 3.5);
        match.setResult(List.of(new Result(1,2,"Away","Draw",List.of(match))));
        matches.add(match);
        when(userService.findByUsername(anyString())).thenReturn(userModel);
        when(resultService.getResults(matchesWithPredict)).thenReturn(matches);
        when(modelMapper.map(any(UserModel.class), eq(User.class))).thenReturn(new User());
        when(betRepository.findByUserUsername(anyString())).thenReturn(Optional.of(new ArrayList<>()));
        when(betRepository.saveAndFlush(any(Bet.class))).thenReturn(new Bet());

        boolean result = betService.createBet(betCreatorModel, "username");

        assertTrue(result);
    }

    @Test
    public void testGetBetsByUsername() {

        List<Bet> bets = new ArrayList<>();
        bets.add(new Bet());
        when(betRepository.findByUserUsername(anyString())).thenReturn(Optional.of(bets));

        List<Bet> result = betService.getBetsByUsername("username");

        assertEquals(bets, result);
    }

    @Test
    public void testFindBetById() {

        String betId = "123";
        Bet bet = new Bet();
        when(betRepository.findById(anyString())).thenReturn(Optional.of(bet));

        Bet result = betService.findBetById(betId);

        assertEquals(bet, result);
    }

    @Test
    void testGetProfit() {

        User user = new User();
        user.setUsername("testUser");
        user.setBalance(BigDecimal.valueOf(100.0));

        Bet bet1 = new Bet();
        bet1.setPrice(BigDecimal.valueOf(10.0));
        bet1.setWinningMoney(BigDecimal.valueOf(20.0));
        bet1.setUser(user);

        Bet bet2 = new Bet();
        bet2.setPrice(BigDecimal.valueOf(20.0));
        bet2.setWinningMoney(BigDecimal.valueOf(0.0));
        bet2.setUser(user);

        List<Bet> bets = Arrays.asList(bet1, bet2);

        when(betRepository.findByUserUsername("testUser")).thenReturn(Optional.of(bets));

        UserModel userModel = new UserModel();
        userModel.setUsername("testUser");
        String profit = betService.getProfit(userModel);
        assertEquals("-10,00", profit);
    }
}
