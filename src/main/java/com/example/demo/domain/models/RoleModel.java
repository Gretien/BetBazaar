package com.example.demo.domain.models;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleModel {
    private String id;
    private String roleType;
}
