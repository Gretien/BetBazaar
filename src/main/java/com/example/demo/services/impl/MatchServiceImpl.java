package com.example.demo.services.impl;

import com.example.demo.domain.entities.Match;
import com.example.demo.domain.entities.Odd;
import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.AddBetModel;
import com.example.demo.repositories.MatchRepository;
import com.example.demo.services.MatchService;
import com.example.demo.services.OddService;
import com.example.demo.services.TeamService;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MatchServiceImpl implements MatchService {
    private TeamService teamService;
    private OddService oddService;
    private MatchRepository matchRepository;

    public MatchServiceImpl(TeamService teamService, OddService oddService, MatchRepository matchRepository) {
        this.teamService = teamService;
        this.oddService = oddService;
        this.matchRepository = matchRepository;
    }

    @Override
    public List<Match> initMatches() {
        if(this.matchRepository.count()==0) {
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance();
            symbols.setDecimalSeparator('.');
            List<Team> teams = this.teamService.findAll();
            List<Match> matches = new ArrayList<>();
            for (int i = 0; i < teams.size(); i+=2) {
                Team home = teams.get(i);
                Team away = teams.get(i + 1);
                Odd homeOdd = getRandomOddInRange(1.20, 2.50);
                Odd drawOdd = getRandomOddInRange(2.20, 4.00);
                Odd awayOdd = getRandomOddInRange(1.80, 3.30);
                DecimalFormat df = new DecimalFormat("#.##", symbols);
                Match match = new Match(home, away,Double.valueOf(df.format(homeOdd.getOdd())),
                        Double.valueOf(df.format(drawOdd.getOdd())),Double.valueOf(df.format(awayOdd.getOdd())));
                matches.add(match);
            }
            this.matchRepository.saveAllAndFlush(matches);
            return matches;
        }
        return this.matchRepository.findAll();
    }

    @Override
    public AddBetModel getMatchInBet(Double odd) {
        AddBetModel addBetModel = null;
        List<Match> matches = initMatches();
        for (Match match : matches) {
            if(match.getHomeOdd().equals(odd)){
                addBetModel = new AddBetModel(match.getHome().getName()+"-"+match.getAway().getName() + ": Home",match.getHomeOdd());
            }else if(match.getDrawOdd().equals(odd)){
                addBetModel = new AddBetModel(match.getHome().getName()+"-"+match.getAway().getName() +": Draw",match.getDrawOdd());
            }else if(match.getAwayOdd().equals(odd)){
                addBetModel = new AddBetModel(match.getHome().getName()+"-"+match.getAway().getName() + ": Away",match.getAwayOdd());
            }
        }
        return addBetModel;
    }

    @Override
    public void addMatch(String fistTeam,String secondTeam) {
        Odd homeOdd = getRandomOddInRange(1.20, 2.50);
        Odd drawOdd = getRandomOddInRange(2.20, 4.00);
        Odd awayOdd = getRandomOddInRange(1.80, 3.30);
        Team homeTeam = this.teamService.findByName(fistTeam);
        Team awayTeam = this.teamService.findByName(secondTeam);
        Match match = new Match(homeTeam,awayTeam,homeOdd.getOdd(),drawOdd.getOdd(),awayOdd.getOdd());
        this.matchRepository.saveAndFlush(match);
    }

    @Override
    public Match findMatchByHomeTeam(String homeTeamId) {
        return this.matchRepository.findByHomeId(homeTeamId).orElse(null);
    }


    private Odd getRandomOddInRange(double lower, double upper) {
        List<Odd> odds = this.oddService.findAll();
        List<Odd> collect = odds.stream().filter(o -> o.getOdd() >= lower && o.getOdd() <= upper).collect(Collectors.toList());
        Random rand = new Random();
        return collect.get(rand.nextInt(collect.size()));
    }
}
