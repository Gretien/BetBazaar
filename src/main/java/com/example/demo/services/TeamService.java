package com.example.demo.services;


import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.TeamAddModel;

import java.util.List;

public interface TeamService {
    boolean addTeams(TeamAddModel teamAddModel);

    List<Team> findAll();

    Team findByName(String fistTeam);
}
