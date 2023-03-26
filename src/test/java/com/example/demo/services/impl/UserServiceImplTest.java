package com.example.demo.services.impl;

import com.example.demo.domain.entities.User;
import com.example.demo.domain.enums.RoleType;
import com.example.demo.domain.models.RoleModel;
import com.example.demo.domain.models.UserModel;
import com.example.demo.domain.models.binding.AmountModel;
import com.example.demo.domain.models.binding.UserRegister;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.RoleService;
import com.example.demo.services.UserService;
import com.example.demo.services.userDetails.ApplicationUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextHolderStrategy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.DelegatingSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private RoleService roleService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private ApplicationUserDetailsService applicationUserDetailsService;

    @Captor
    private ArgumentCaptor<User> userArgumentCaptor;

    @InjectMocks
    private UserServiceImpl userService;

    public UserServiceImplTest() {
    }

    @Before
    public void setup() {
        userService = new UserServiceImpl(userRepository,modelMapper,roleService,passwordEncoder,userDetailsService,applicationUserDetailsService);
    }

    @Test
    public void initAdmin_whenNoUsersInDatabase_shouldCreateAdminUser() {
        Mockito.when(userRepository.count()).thenReturn(0L);
        Mockito.when(roleService.findByName()).thenReturn(new RoleModel("4853b13f-6979-4d44-8494-28e65f275d55","ADMIN"));
        Mockito.when(passwordEncoder.encode("12345")).thenReturn("encodedPassword");

        userService.initAdmin();

        User user = verify(userRepository).saveAndFlush(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();
        Assertions.assertEquals("Admin", savedUser.getUsername());
        Assertions.assertEquals("encodedPassword", savedUser.getPassword());
        Assertions.assertEquals(BigDecimal.valueOf(1000), savedUser.getBalance());
        Assertions.assertEquals(1, savedUser.getRoles().size());
    }

    @Test
    public void register_shouldCreateUserAndReturnAuthentication() {
        UserRegister userRegister = new UserRegister();
        userRegister.setUsername("testuser");
        userRegister.setPassword("password");
        userRegister.setFirstName("Test");
        userRegister.setLastName("User");
        userRegister.setAge(25);

        UserModel userModel = new UserModel();
        userModel.setUsername("testuser");
        userModel.setPassword("password");
        userModel.setFirstName("Test");
        userModel.setLastName("User");
        userModel.setAge(25);
        Mockito.when(modelMapper.map(userModel, User.class)).thenReturn(new User());
        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(UserModel.class))).thenReturn(userModel);
        Mockito.when(userDetailsService.loadUserByUsername("testuser")).thenReturn((UserDetails) new User("testuser", "password", new ArrayList<>()));
        Mockito.doAnswer(invocation -> {
            Consumer<Authentication> successfulLoginProcessor = invocation.getArgument(1);
            successfulLoginProcessor.accept(new UsernamePasswordAuthenticationToken(null, null, null));
            return null;
        }).when(userService).register(Mockito.eq(userRegister), Mockito.any());

        userService.register(userRegister, (authentication) -> {
            Assertions.assertNotNull(authentication);
            Assertions.assertTrue(authentication.isAuthenticated());
            Assertions.assertEquals("testuser", authentication.getName());
        });
    }

    @Test
    public void deposit_shouldIncreaseUserBalance() {
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(BigDecimal.valueOf(100));

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        userService.deposit(new AmountModel("50"), "testuser");

        ArgumentCaptor<User> userArgumentCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).saveAndFlush(userArgumentCaptor.capture());

        User savedUser = userArgumentCaptor.getValue();
        Assertions.assertEquals(BigDecimal.valueOf(150), savedUser.getBalance());
    }

    @Test
    public void findByUsername_whenUserNotFound_shouldThrowNoSuchElementException() {
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        assertThrows(
                NoSuchElementException.class,
                () -> userService.findByUsername("testuser")
        );
    }

    @Test
    public void withdraw_shouldDecreaseUserBalance() {
        User user = new User();
        user.setUsername("testuser");
    }
}
