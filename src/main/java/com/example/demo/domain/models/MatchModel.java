package com.example.demo.domain.models;

import com.example.demo.domain.entities.Result;
import com.example.demo.domain.entities.Team;
import jakarta.persistence.Column;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MatchModel {
    private String id;

    private TeamModel home;

    private TeamModel away;

    private Double homeOdd;

    private Double drawOdd;

    private Double awayOdd;

    private ResultModel result;
}
