package com.example.demo.repositories;

import com.example.demo.domain.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeamRepository extends JpaRepository<Team, String> {
    Optional<Team> findByName(String name);
}
