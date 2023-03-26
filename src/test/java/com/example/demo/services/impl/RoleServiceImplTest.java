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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
        // Test that conditions are saved when there are no roles in the database
        when(roleRepository.count()).thenReturn(0L);

        List<Role> roles = Arrays.stream(RoleType.values())
                .map(type -> Role.builder().roleType(type).build())
                .collect(Collectors.toList());
        List<RoleModel> roleModels = Arrays.stream(RoleType.values())
                .map(type -> RoleModel.builder().roleType(type.name()).build())
                .collect(Collectors.toList());
        when(modelMapper.map(any(RoleModel.class), eq(Role.class))).thenReturn(new Role());
        when(roleRepository.saveAllAndFlush(anyList())).thenReturn(roles);

        roleService.init();

        verify(roleRepository).saveAllAndFlush(roleArgumentCaptor.capture());
        List<Role> roleList = roleArgumentCaptor.getValue();
        Assertions.assertEquals(roles.size(),roleList.size());
        Assertions.assertEquals(roles.get(0).getRoleType().name(),roleList.get(0).getRoleType().name());
    }

    @Test
    public void testFindByName() {
        // Test that findByName returns the correct RoleModel
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
        // Test that setToUser returns the correct RoleModel
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
