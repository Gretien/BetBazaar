package com.example.demo.domain.models.binding;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BetCreatorModel {
    private List<AddBetModel> matches;
    private BigDecimal price;
    private Double totalOdds;
}
