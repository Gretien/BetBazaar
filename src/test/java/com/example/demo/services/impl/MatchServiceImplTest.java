package com.example.demo.services.impl;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Odd;
import com.example.demo.domain.entities.Team;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.repositories.MatchRepository;
import com.example.demo.services.OddService;
import com.example.demo.services.TeamService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MatchServiceImplTest {

    @Mock
    private TeamService teamService;

    @Mock
    private OddService oddService;

    @Mock
    private MatchRepository matchRepository;

    @Captor
    private ArgumentCaptor<Match> matchArgumentCaptor;

    @InjectMocks
    private MatchServiceImpl matchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        matchService = new MatchServiceImpl(teamService, oddService, matchRepository);
    }

    @Test
    public void initMatches_should_return_list_of_matches_when_no_matches_exist() {
        List<Team> teams = new ArrayList<>();
        teams.add(new Team("Team A"));
        teams.add(new Team("Team B"));
        teams.add(new Team("Team C"));
        teams.add(new Team("Team D"));
        when(teamService.findAll()).thenReturn(teams);

        List<Match> matches = new ArrayList<>();
        Match match1 = new Match(teams.get(0), teams.get(1), 1.5, 2.5, 3.5);
        Match match2 = new Match(teams.get(2), teams.get(3), 2.0, 3.0, 4.0);
        matches.add(match1);
        matches.add(match2);
        when(matchRepository.count()).thenReturn((long) 0);
        List<Odd> odds = new ArrayList<>();
        odds.add(new Odd(1.25));
        odds.add(new Odd(1.75));
        odds.add(new Odd(2.25));
        odds.add(new Odd(2.75));
        odds.add(new Odd(3.25));
        odds.add(new Odd(3.75));
        when(oddService.findAll()).thenReturn(odds);


        List<Match> actualMatches = matchService.initMatches();

        assertEquals(matches.size(), actualMatches.size());
    }

    @Test
    public void initMatches_should_return_list_of_matches_when_matches_exist() {
        List<Match> matches = new ArrayList<>();
        Match match1 = new Match(new Team("Team A"), new Team("Team B"), 1.5, 2.5, 3.5);
        Match match2 = new Match(new Team("Team C"), new Team("Team D"), 2.0, 3.0, 4.0);
        matches.add(match1);
        matches.add(match2);
        when(matchRepository.count()).thenReturn((long) matches.size());
        when(matchRepository.findAll()).thenReturn(matches);

        List<Match> actualMatches = matchService.initMatches();

        assertEquals(matches.size(), actualMatches.size());
    }

    @Test
    public void getMatchInBet_should_return_AddBetModel_when_match_exists_with_given_odd() {
        Double odd = 1.5;
        List<Match> matches = new ArrayList<>();
        Match match1 = new Match(new Team("Team A"), new Team("Team B"), 1.5, 2.5, 3.5);
        Match match2 = new Match(new Team("Team C"), new Team("Team D"), 2.0, 3.0, 4.0);
        matches.add(match1);
        matches.add(match2);
        when(matchRepository.count()).thenReturn((long) matches.size());
        when(matchRepository.findAll()).thenReturn(matches);

        AddBetModel expectedAddBetModel = new AddBetModel("Team A-Team B: Home", 1.5);

        AddBetModel actualAddBetModel = matchService.getMatchInBet(odd);

        assertEquals(expectedAddBetModel.getOdd(), actualAddBetModel.getOdd());
    }

    @Test
    public void testAddMatch() {
        String firstTeam = "Team A";
        String secondTeam = "Team B";
        Odd homeOdd = new Odd(1.5);
        Odd drawOdd = new Odd(2.5);
        Odd awayOdd = new Odd(3.5);
        Team homeTeam = new Team(firstTeam);
        Team awayTeam = new Team(secondTeam);
        List<Odd> odds = new ArrayList<>();
        odds.add(new Odd(1.5));
        odds.add(new Odd(2.5));
        odds.add(new Odd(3.5));
        when(oddService.findAll()).thenReturn(odds);
        when(teamService.findByName("Team A")).thenReturn(homeTeam);
        when(teamService.findByName("Team B")).thenReturn(awayTeam);
        Match expectedMatch = new Match(homeTeam, awayTeam, homeOdd.getOdd(), drawOdd.getOdd(), awayOdd.getOdd());

        matchService.addMatch(firstTeam, secondTeam);

        verify(teamService, times(1)).findByName(firstTeam);
        verify(teamService, times(1)).findByName(secondTeam);
        verify(matchRepository).saveAndFlush(matchArgumentCaptor.capture());

        Match match = matchArgumentCaptor.getValue();

        assertEquals(expectedMatch.getHome(), match.getHome());
        assertEquals(expectedMatch.getAway(), match.getAway());
    }

    @Test
    void testFindMatchByHomeTeam() {
        Team homeTeam = new Team("Barcelona");
        homeTeam.setId("1");
        Team awayTeam = new Team("Real Madrid");
        Odd homeOdd = new Odd(1.5);
        Odd drawOdd = new Odd(2.5);
        Odd awayOdd = new Odd(3.5);
        Match match = new Match(homeTeam, awayTeam, homeOdd.getOdd(), drawOdd.getOdd(), awayOdd.getOdd());
        when(matchRepository.findByHomeId("1")).thenReturn(Optional.of(match));
        Match foundMatch = matchService.findMatchByHomeTeam(homeTeam.getId());

        assertNotNull(foundMatch);
        assertEquals(homeTeam, foundMatch.getHome());
    }
}