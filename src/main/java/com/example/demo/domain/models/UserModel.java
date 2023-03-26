package com.example.demo.domain.models;

import com.example.demo.domain.entities.Bet;
import com.example.demo.domain.entities.Post;
import com.example.demo.domain.entities.Role;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserModel {
    private String id;

    private String username;

    private String password;

    private Integer age;

    private String firstName;

    private String lastName;

    private BigDecimal balance;

    private List<BetModel> bets;

    private List<PostModel> posts;

    private List<RoleModel> roles;
}
