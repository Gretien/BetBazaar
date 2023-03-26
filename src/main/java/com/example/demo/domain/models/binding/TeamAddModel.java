package com.example.demo.domain.models.binding;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamAddModel {
    @NotNull
    @Size(min = 3,max = 30)
    private String firstName;
    @NotNull
    @Size(min = 3,max = 30)
    private String secondName;
}
