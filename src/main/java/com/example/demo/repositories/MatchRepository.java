package com.example.demo.repositories;

import com.example.demo.domain.entities.Match;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MatchRepository extends JpaRepository<Match,String> {

    Optional<Match> findByHomeId(String id);
}
