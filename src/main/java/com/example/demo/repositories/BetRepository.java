package com.example.demo.repositories;

import com.example.demo.domain.entities.Bet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BetRepository extends JpaRepository<Bet,String> {

    Optional<List<Bet>> findByUserUsername(String username);
}
