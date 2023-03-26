package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "results")
public class Result extends BaseEntity{
    @Column(name = "home_goals")
    private Integer homeGoals;
    @Column(name = "away_goals")
    private Integer awayGoals;
    @Column(nullable = false)
    private String winner;
    @Column
    private String prediction;
    @ManyToMany
    private List<Match> match = new ArrayList<>();
}
