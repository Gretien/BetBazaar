package com.example.demo.services.impl;

import com.example.demo.domain.entities.Role;
import com.example.demo.domain.enums.RoleType;
import com.example.demo.domain.models.RoleModel;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.RoleService;
import jakarta.annotation.PostConstruct;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;
    private ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @PostConstruct
    public void init() {
        if (this.roleRepository.count() == 0) {
            List<Role> conditions = Arrays.stream(RoleType.values()).map(type -> RoleModel.builder()
                            .roleType(type.name()).build())
                    .map(roleModel -> this.modelMapper.map(roleModel, Role.class)).collect(Collectors.toList());

            this.roleRepository.saveAllAndFlush(conditions);
        }
    }

    @Override
    public RoleModel findByName() {
        Role role = this.roleRepository.findByRoleType(RoleType.ADMIN);
        RoleModel roleModel = this.modelMapper.map(role, RoleModel.class);
        return roleModel;
    }

    @Override
    public RoleModel setToUser() {
        Role role = this.roleRepository.findByRoleType(RoleType.USER);
        RoleModel roleModel = this.modelMapper.map(role, RoleModel.class);
        return roleModel;
    }
}
