package com.example.demo.services.impl;

import com.example.demo.domain.entities.Odd;
import com.example.demo.domain.entities.Role;
import com.example.demo.domain.enums.RoleType;
import com.example.demo.domain.models.RoleModel;
import com.example.demo.repositories.RoleRepository;
import com.example.demo.services.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.lang.model.util.Types;
import java.sql.Connection;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RoleServiceImplTest {
    @Mock
    private RoleRepository roleRepository;
    @Mock
    private ModelMapper modelMapper;
    @InjectMocks
    private RoleServiceImpl roleService;
    @Captor
    private ArgumentCaptor<List<Role>> roleArgumentCaptor;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInit() {

        when(roleRepository.count()).thenReturn(0L);

        RoleType[] roleTypes = RoleType.values();
        List<RoleModel> roleModels = Arrays.stream(roleTypes)
                .map(type -> RoleModel.builder().roleType(type.name()).build())
                .collect(Collectors.toList());

        List<Role> expectedRoles = roleModels.stream()
                .map(roleModel -> modelMapper.map(roleModel, Role.class))
                .collect(Collectors.toList());

        roleService.init();

        verify(roleRepository, times(1)).count();
        verify(roleRepository, times(1)).saveAllAndFlush(expectedRoles);
    }

    @Test
    public void testFindByName() {

        Role role = new Role();
        role.setRoleType(RoleType.ADMIN);
        when(roleRepository.findByRoleType(RoleType.ADMIN)).thenReturn(role);

        RoleModel roleModel = new RoleModel();
        when(modelMapper.map(role, RoleModel.class)).thenReturn(roleModel);

        RoleModel result = roleService.findByName();

        assertEquals(roleModel, result);

    }

    @Test
    public void testSetToUser() {

        Role role = new Role();
        role.setRoleType(RoleType.USER);
        when(roleRepository.findByRoleType(RoleType.USER)).thenReturn(role);

        RoleModel roleModel = new RoleModel();
        when(modelMapper.map(role, RoleModel.class)).thenReturn(roleModel);

        RoleModel result = roleService.setToUser();

        assertEquals(roleModel, result);
        verify(roleRepository).findByRoleType(RoleType.USER);
        verify(modelMapper).map(role, RoleModel.class);
    }
}
