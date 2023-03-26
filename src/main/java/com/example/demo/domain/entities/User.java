package com.example.demo.domain.entities;

import jakarta.persistence.*;
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
@Entity
@Table(name = "users")
public class User extends BaseEntity{
    @Column(unique = true,nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @Column(nullable = false)
    private Integer age;
    @Column(nullable = false,name = "first_name")
    private String firstName;
    @Column(nullable = false,name = "last_name")
    private String lastName;
    @Column(nullable = false)
    private BigDecimal balance;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Bet> bets = new ArrayList<>();
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user")
    private List<Post> posts = new ArrayList<>();
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Role> roles = new ArrayList<>();


    public User(String username,String password, List<Role> roles) {
        super();
        this.password = password;
        this.username = username;
        this.roles = roles;
    }
}
