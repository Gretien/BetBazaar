package com.example.demo.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "bets")
public class Bet extends BaseEntity{
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Match> matches = new ArrayList<>();
    @Column(nullable = false)
    private Double odds;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false)
    private BigDecimal winningMoney;
    @Column
    @ColumnDefault("false")
    private Boolean isWinning;
    @Column(nullable = false)
    private Integer numberOfBet;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
