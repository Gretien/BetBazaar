package com.example.demo.services;

import com.example.demo.domain.entities.Role;
import com.example.demo.domain.models.RoleModel;

public interface RoleService {

    RoleModel findByName();

    RoleModel setToUser();
}
