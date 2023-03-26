package com.example.demo.services.impl;

import com.example.demo.domain.entities.Team;
import com.example.demo.domain.models.binding.TeamAddModel;
import com.example.demo.domain.models.seed.TeamSeedModel;
import com.example.demo.repositories.TeamRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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

    @Test
    public void testInit() throws IOException {
        // Given
        when(teamRepository.count()).thenReturn(0L);

        List<Team> teams = Arrays.asList(
                new Team("Team A"),
                new Team("Team B"),
                new Team("Team C"));

        when(modelMapper.map(any(TeamSeedModel.class), eq(Team.class))).thenReturn(teams.get(0), teams.get(1), teams.get(2));

        // When
        teamService.init();

        // Then
        verify(teamRepository).saveAllAndFlush(teamCaptor.capture());
        List<Team> teamList = teamCaptor.getValue();
        assertEquals(10,teamList.size());
        assertEquals(teams.get(0).getName(),teamList.get(0).getName());
    }

    @Test
    public void testAddTeams() {
        // Given
        TeamAddModel teamAddModel = new TeamAddModel("Team A", "Team B");

        when(teamRepository.findByName("Team A")).thenReturn(Optional.empty());
        when(teamRepository.findByName("Team B")).thenReturn(Optional.empty());

        // When
        boolean result = teamService.addTeams(teamAddModel);

        // Then
        assertTrue(result);
    }

    @Test
    public void testAddTeamsTeamAlreadyExists() {
        // Given
        TeamAddModel teamAddModel = new TeamAddModel("Team A", "Team B");

        when(teamRepository.findByName("Team A")).thenReturn(Optional.of(new Team("Team A")));

        // When
        boolean result = teamService.addTeams(teamAddModel);

        // Then
        assertFalse(result);
        verify(teamRepository, never()).saveAndFlush(any(Team.class));
    }

    @Test
    public void testFindAll() {
        // Given
        List<Team> teams = Arrays.asList(
                new Team("Team A"),
                new Team("Team B"),
                new Team("Team C"));

        when(teamRepository.findAll()).thenReturn(teams);

        // When
        List<Team> result = teamService.findAll();

        // Then
        assertEquals(teams, result);
    }

    @Test
    public void testFindByName() {
        // Given
        Team team = new Team("Team A");

        when(teamRepository.findByName("Team A")).thenReturn(Optional.of(team));

        // When
        Team result = teamService.findByName("Team A");

        // Then
        assertEquals(team, result);
    }

    @Test
    public void testFindByNameTeamNotFound() {
        // Given
        when(teamRepository.findByName("Team A")).thenReturn(Optional.empty());

        assertThrows(
                NoSuchElementException.class,
                () -> teamService.findByName("Team A")
        );

    }
}
