package com.example.demo.services.impl;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Result;
import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.repositories.ResultRepository;
import com.example.demo.services.MatchService;
import com.example.demo.services.TeamService;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ResultServiceImplTest {

    @Mock
    private ResultRepository resultRepository;

    @Mock
    private TeamService teamService;

    @Mock
    private MatchService matchService;

    @InjectMocks
    private ResultServiceImpl resultService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);

    }

    @Test
    public void testGetResults() {

        AddBetModel betModel1 = new AddBetModel("Team A-Team B: Home",2.0);
        AddBetModel betModel2 = new AddBetModel("Team C-Team D: Away",2.0);

        Team homeTeam1 = new Team("Team A");
        homeTeam1.setId("1");
        Team homeTeam2 = new Team("Team C");
        homeTeam2.setId("2");
        Match match1 = new Match(homeTeam1,new Team(),1.0,1.0,1.0);
        Match match2 = new Match(homeTeam2, new Team(),2.0,2.0,2.0);
        when(teamService.findByName("Team A")).thenReturn(homeTeam1);
        when(teamService.findByName("Team C")).thenReturn(homeTeam2);
        when(matchService.findMatchByHomeTeam("1")).thenReturn(match1);
        when(matchService.findMatchByHomeTeam("2")).thenReturn(match2);

        List<Match> results = resultService.getResults(List.of(betModel1, betModel2));

        assertEquals(2, results.size());
        assertNotNull(results.get(0).getResult());
        assertNotNull(results.get(1).getResult());

        assertEquals("Home", results.get(0).getResult().get(0).getPrediction());
        assertEquals("Away", results.get(1).getResult().get(0).getPrediction());

        verify(resultRepository, times(2)).saveAndFlush(any(Result.class));
    }

}
