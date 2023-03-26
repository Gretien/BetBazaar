package com.example.demo.repositories;

import com.example.demo.domain.entities.Result;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ResultRepository extends JpaRepository<Result,String> {
}
