package com.example.demo.services;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.AddBetModel;

import java.util.List;

public interface MatchService {
    List<Match> initMatches();

    AddBetModel getMatchInBet(Double odd);

    void addMatch(String fistTeam,String secondTeam);

    Match findMatchByHomeTeam(String homeTeamId);
}
