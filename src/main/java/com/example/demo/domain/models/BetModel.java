package com.example.demo.domain.models;

import com.example.demo.domain.entities.Match;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetModel {

    private String id;

    private List<MatchModel> matches;

    private Double odds;

    private BigDecimal price;

    private BigDecimal winningMoney;

    private Boolean isWinning;

}
