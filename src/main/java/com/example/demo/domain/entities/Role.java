package com.example.demo.domain.entities;

import com.example.demo.domain.enums.RoleType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
@Builder
public class Role extends BaseEntity{
    @Column(unique = true,nullable = false)
    @Enumerated(EnumType.STRING)
    private RoleType roleType;
}
