package com.example.demo.services.impl;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Result;
import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.repositories.ResultRepository;
import com.example.demo.services.MatchService;
import com.example.demo.services.ResultService;
import com.example.demo.services.TeamService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ResultServiceImpl implements ResultService {
    private ResultRepository resultRepository;
    private TeamService teamService;
    private MatchService matchService;


    public ResultServiceImpl(ResultRepository resultRepository, TeamService teamService, MatchService matchService) {
        this.resultRepository = resultRepository;
        this.teamService = teamService;
        this.matchService = matchService;
    }

    @Override
    public List<Match> getResults(List<AddBetModel> matches) {
        List<Match> matchesWithResults = new ArrayList<>();
        for (AddBetModel match : matches) {
            String[] data = match.getName().split(": ");
            String prediction = data[1];
            String[] teams = data[0].split("-");
            Team homeTeam = this.teamService.findByName(teams[0]);
            Match matchToSave = this.matchService.findMatchByHomeTeam(homeTeam.getId());
            Result result = getResult(matchToSave,prediction);
            if(matchToSave.getResult().size()==0) {
                matchToSave.getResult().add(result);
            }else {
                matchToSave.getResult().set(0,result);
            }
            matchesWithResults.add(matchToSave);
            this.resultRepository.saveAndFlush(result);
        }
        return matchesWithResults;
    }

    private Result getResult(Match matchToSave, String prediction) {
        Random random = new Random();
        int min = 0;
        int max = 5;
        int homeGoals = random.nextInt((max - min) + 1) + min;
        int awayGoals = random.nextInt((max - min) + 1) + min;
        String winner="";
        if(homeGoals>awayGoals){
            winner = "Home";
        }else if(homeGoals==awayGoals){
            winner = "Draw";
        }else {
            winner = "Away";
        }
        Result result = new Result(homeGoals,awayGoals,winner,prediction,List.of(matchToSave));
        return result;
    }
}
