package com.example.demo.services.impl;

import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.TeamAddModel;
import com.example.demo.domain.models.seed.TeamSeedModel;
import com.example.demo.repositories.TeamRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamServiceImplTest {

    @Mock
    private TeamRepository teamRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private TeamServiceImpl teamService;
    @Captor
    private ArgumentCaptor<List<Team>> teamCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInit() throws IOException {

        when(teamRepository.count()).thenReturn(0L);

        List<Team> teams = Arrays.asList(
                new Team("Team A"),
                new Team("Team B"),
                new Team("Team C"));

        when(modelMapper.map(any(TeamSeedModel.class), eq(Team.class))).thenReturn(teams.get(0), teams.get(1), teams.get(2));

        teamService.init();

        verify(teamRepository).saveAllAndFlush(teamCaptor.capture());
        List<Team> teamList = teamCaptor.getValue();
        assertEquals(10,teamList.size());
        assertEquals(teams.get(0).getName(),teamList.get(0).getName());
    }

    @Test
    public void testAddTeams() {

        TeamAddModel teamAddModel = new TeamAddModel("Team A", "Team B");

        when(teamRepository.findByName("Team A")).thenReturn(Optional.empty());
        when(teamRepository.findByName("Team B")).thenReturn(Optional.empty());

        boolean result = teamService.addTeams(teamAddModel);

        assertTrue(result);
    }

    @Test
    public void testAddTeamsTeamAlreadyExists() {

        TeamAddModel teamAddModel = new TeamAddModel("Team A", "Team B");

        when(teamRepository.findByName("Team A")).thenReturn(Optional.of(new Team("Team A")));

        boolean result = teamService.addTeams(teamAddModel);

        assertFalse(result);
        verify(teamRepository, never()).saveAndFlush(any(Team.class));
    }

    @Test
    public void testFindAll() {

        List<Team> teams = Arrays.asList(
                new Team("Team A"),
                new Team("Team B"),
                new Team("Team C"));

        when(teamRepository.findAll()).thenReturn(teams);

        List<Team> result = teamService.findAll();

        assertEquals(teams, result);
    }

    @Test
    public void testFindByName() {

        Team team = new Team("Team A");

        when(teamRepository.findByName("Team A")).thenReturn(Optional.of(team));

        Team result = teamService.findByName("Team A");

        assertEquals(team, result);
    }

    @Test
    public void testFindByNameTeamNotFound() {

        when(teamRepository.findByName("Team A")).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> teamService.findByName("Team A")
        );

    }
}
