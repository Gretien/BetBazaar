package com.example.demo.repositories;

import com.example.demo.domain.entities.Odd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OddRepository extends JpaRepository<Odd, String> {

}
