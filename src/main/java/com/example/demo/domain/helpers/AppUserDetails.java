package com.example.demo.domain.helpers;

import com.example.demo.domain.entities.Bet;
import com.example.demo.domain.entities.Post;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


public class AppUserDetails extends User {

    private Integer age;

    private String firstName;

    private String lastName;

    private BigDecimal balance;

    private List<Bet> bets = new ArrayList<>();

    private List<Post> posts = new ArrayList<>();
    public AppUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public Integer getAge() {
        return age;
    }

    public AppUserDetails setAge(Integer age) {
        this.age = age;
        return this;
    }

    public String getFirstName() {
        return firstName;
    }

    public AppUserDetails setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public String getLastName() {
        return lastName;
    }

    public AppUserDetails setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public AppUserDetails setBalance(BigDecimal balance) {
        this.balance = balance;
        return this;
    }

    public List<Bet> getBets() {
        return bets;
    }

    public AppUserDetails setBets(List<Bet> bets) {
        this.bets = bets;
        return this;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public AppUserDetails setPosts(List<Post> posts) {
        this.posts = posts;
        return this;
    }
}
