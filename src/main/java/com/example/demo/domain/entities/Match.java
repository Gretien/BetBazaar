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
@Table(name = "matches")
public class Match extends BaseEntity{
    @ManyToOne
    private Team home;
    @ManyToOne
    private Team away;
    @Column(name = "home_odd",nullable = false)
    private Double homeOdd;
    @Column(name = "draw_odd",nullable = false)
    private Double drawOdd;
    @Column(name = "away_odd",nullable = false)
    private Double awayOdd;
    @ManyToMany
    private List<Result> result = new ArrayList<>();

    public Match(Team home, Team away, Double homeOdd, Double drawOdd, Double awayOdd) {
        this.home = home;
        this.away = away;
        this.homeOdd = homeOdd;
        this.drawOdd = drawOdd;
        this.awayOdd = awayOdd;
    }
}
