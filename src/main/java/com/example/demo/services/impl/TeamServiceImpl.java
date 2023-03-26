package com.example.demo.services.impl;

import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.TeamAddModel;
import com.example.demo.domain.models.seed.TeamSeedModel;
import com.example.demo.repositories.MatchRepository;
import com.example.demo.repositories.TeamRepository;
import com.example.demo.services.TeamService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TeamServiceImpl implements TeamService {

    private TeamRepository teamRepository;
    private ModelMapper modelMapper;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, ModelMapper modelMapper) {
        this.teamRepository = teamRepository;
        this.modelMapper = modelMapper;
    }


    @PostConstruct
    public void init() throws IOException {
        if (this.teamRepository.count() == 0) {
            List<Team> teams = new ArrayList<>();
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/teams.txt"));
            String teamName;
            while ((teamName = br.readLine()) != null) {
                TeamSeedModel teamModel = new TeamSeedModel(teamName);
                Team team = this.modelMapper.map(teamModel, Team.class);
                teams.add(team);
            }
            br.close();
            this.teamRepository.saveAllAndFlush(teams);
        }
    }

    @Override
    public boolean addTeams(TeamAddModel teamAddModel) {
        Team fistTeam = new Team(teamAddModel.getFirstName());
        Team secondTeam = new Team(teamAddModel.getSecondName());

        if (this.teamRepository.findByName(fistTeam.getName()).isPresent() || this.teamRepository.findByName(secondTeam.getName()).isPresent()) {
            return false;
        }

        this.teamRepository.saveAndFlush(fistTeam);
        this.teamRepository.saveAndFlush(secondTeam);

        return true;
    }

    @Override
    public List<Team> findAll() {
        return this.teamRepository.findAll();
    }

    @Override
    public Team findByName(String fistTeam) {
        return this.teamRepository.findByName(fistTeam).orElseThrow(NoSuchElementException::new);
    }
}

