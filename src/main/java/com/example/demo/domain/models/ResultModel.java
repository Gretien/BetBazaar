package com.example.demo.domain.models;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResultModel {
    private String id;

    private Integer homeGoals;

    private Integer awayGoals;

    private String winner;

    private String prediction;
}
