package com.example.demo.services.impl;

import com.example.demo.domain.entities.Role;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.enums.RoleType;
import com.example.demo.domain.helpers.AppUserDetails;
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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.function.Consumer;


import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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



    @Before
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testInitAdmin() {
        RoleModel roleModel = new RoleModel();
        roleModel.setRoleType("ADMIN");

        UserModel userModel = UserModel.builder()
                .username("Admin")
                .password("12345")
                .firstName("Admin")
                .lastName("Adminov")
                .age(20)
                .balance(BigDecimal.valueOf(1000))
                .bets(new ArrayList<>())
                .posts(new ArrayList<>())
                .roles(List.of(roleModel))
                .build();

        User user = new User();
        user.setUsername("Admin");
        user.setPassword("encodedPassword");
        user.setFirstName("Admin");
        user.setLastName("Adminov");
        user.setAge(20);
        user.setBalance(BigDecimal.valueOf(1000));
        user.setBets(new ArrayList<>());
        user.setPosts(new ArrayList<>());
        user.setRoles(List.of(new Role(RoleType.ADMIN)));

        when(userRepository.count()).thenReturn(0L);
        when(roleService.findByName()).thenReturn(roleModel);
        when(passwordEncoder.encode(any(String.class))).thenReturn("encodedPassword");
        when(modelMapper.map(any(UserModel.class),eq(User.class))).thenReturn(user);
        when(userRepository.saveAndFlush(any(User.class))).thenReturn(user);

        userService.initAdmin();

        verify(userRepository, times(1)).count();
        verify(roleService, times(1)).findByName();
        verify(passwordEncoder, times(1)).encode("12345");
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    public void testRegister() {
        UserRegister userRegister = new UserRegister();
        userRegister.setUsername("testuser");
        userRegister.setFirstName("Test");
        userRegister.setLastName("User");
        userRegister.setAge(25);
        userRegister.setPassword("password");


        User user = new User();
        user.setUsername("testuser");
        user.setFirstName("Test");
        user.setLastName("User");
        user.setAge(25);
        user.setPassword("password");
        RoleModel roleModel = new RoleModel();
        roleModel.setId("1");
        roleModel.setRoleType(RoleType.USER.name());

        when(roleService.setToUser()).thenReturn(roleModel);
        when(modelMapper.map(any(UserModel.class),eq(User.class))).thenReturn(user);
        when(passwordEncoder.encode(any(String.class))).thenReturn("password");
        UserDetails userDetails = new AppUserDetails(userRegister.getUsername(),userRegister.getPassword(),new ArrayList<>());
        when(userDetailsService.loadUserByUsername(userRegister.getUsername())).thenReturn(userDetails);


        userService.register(userRegister, authentication -> {});

        verify(roleService, times(1)).setToUser();
        verify(userDetailsService, times(1)).loadUserByUsername(userRegister.getUsername());
        verify(userRepository, times(1)).saveAndFlush(userArgumentCaptor.capture());

        User savedUser =  userArgumentCaptor.getValue();
        Assertions.assertEquals(userRegister.getUsername(), savedUser.getUsername());
        Assertions.assertEquals(userRegister.getFirstName(), savedUser.getFirstName());
        Assertions.assertEquals(userRegister.getLastName(), savedUser.getLastName());
        Assertions.assertEquals(userRegister.getAge(), savedUser.getAge());
        Assertions.assertEquals(userRegister.getPassword(), savedUser.getPassword());
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
    public void findByUsername() {
        User user = new User();
        user.setUsername("testuser");
        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));
        UserModel expected = new UserModel();
        expected.setUsername("testuser");
        when(modelMapper.map(any(User.class),eq(UserModel.class))).thenReturn(expected);
        UserModel userModel = userService.findByUsername("testuser");
        Assertions.assertEquals(expected.getUsername(),userModel.getUsername());

    }


    @Test
    public void testAddWinningMoney() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100));
        double winningAmount = 50.0;
        BigDecimal expectedBalance = user.getBalance().add(BigDecimal.valueOf(winningAmount));

        userService.addWinningMoney(user, winningAmount);

        Assertions.assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    public void testSubtractMoneyForBet() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100));
        BigDecimal price = BigDecimal.valueOf(50.0);
        BigDecimal expectedBalance = user.getBalance().subtract(price);

        userService.subtractMoneyForBet(user, price);

        Assertions.assertEquals(expectedBalance, user.getBalance());
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    public void testWithdrawSuccessful() {
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(BigDecimal.valueOf(100));
        AmountModel amountModel = new AmountModel();
        amountModel.setAmount("50");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.withdraw(amountModel, "testuser");

        Assertions.assertTrue(result);
        Assertions.assertEquals(user.getBalance(), BigDecimal.valueOf(50));
        verify(userRepository, times(1)).saveAndFlush(user);
    }

    @Test
    public void testWithdrawInsufficientFunds() {
        User user = new User();
        user.setUsername("testuser");
        user.setBalance(BigDecimal.valueOf(100));
        AmountModel amountModel = new AmountModel();
        amountModel.setAmount("150");
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        boolean result = userService.withdraw(amountModel, "testuser");

        Assertions.assertFalse(result);
        Assertions.assertEquals(user.getBalance(), BigDecimal.valueOf(100));
        verify(userRepository, never()).saveAndFlush(any(User.class));
    }

    @Test
    public void testFindAll() {
        List<User> userList = new ArrayList<>();
        User user1 = new User();
        user1.setId("1");
        userList.add(user1);
        User user2 = new User();
        user2.setId("2");
        userList.add(user2);
        when(userRepository.findAll()).thenReturn(userList);

        List<User> result = userService.findAll();

        Assertions.assertEquals(userList, result);
        verify(userRepository, times(1)).findAll();
    }

    @Test
    public void testDailyBonus() {
        User user = new User();
        user.setBalance(BigDecimal.valueOf(100));
        userService.dailyBonus(user);

        Assertions.assertEquals(BigDecimal.valueOf(110), user.getBalance());
        verify(userRepository, times(1)).saveAndFlush(user);
    }
}
