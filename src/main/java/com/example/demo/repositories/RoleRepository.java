package com.example.demo.repositories;

import com.example.demo.domain.entities.Role;
import com.example.demo.domain.enums.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,String> {
    Role findByRoleType(RoleType roleType);
}
