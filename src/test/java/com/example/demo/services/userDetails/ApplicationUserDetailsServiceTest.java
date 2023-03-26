package com.example.demo.services.userDetails;

import com.example.demo.domain.entities.Role;
import com.example.demo.domain.entities.User;
import com.example.demo.domain.enums.RoleType;
import com.example.demo.repositories.UserRepository;
import junit.framework.AssertionFailedError;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ApplicationUserDetailsServiceTest {

    private ApplicationUserDetailsService applicationUserDetailsService;
    @Mock
    private UserRepository userRepository;


    @BeforeEach
    void setUp() {
        applicationUserDetailsService = new ApplicationUserDetailsService(
                userRepository
        );
    }

    @Test
    void testUserNotFound() {
        assertThrows(
                UsernameNotFoundException.class,
                () -> applicationUserDetailsService.loadUserByUsername("invalid")
        );
    }

    @Test
    void  testUserFound(){
        User testUser = new User("admin","password",List.of(new Role(RoleType.USER),new Role(RoleType.ADMIN)));

        when(userRepository.findByUsername("admin")).
                thenReturn(Optional.of(testUser));


        UserDetails adminDetails =
                applicationUserDetailsService.loadUserByUsername("admin");

        Assertions.assertNotNull(adminDetails);
        Assertions.assertEquals("admin", adminDetails.getUsername());
        Assertions.assertEquals(testUser.getPassword(), adminDetails.getPassword());
        Assertions.assertEquals(2,
                adminDetails.getAuthorities().size());

        assertRole(adminDetails.getAuthorities(), "ROLE_ADMIN");
        assertRole(adminDetails.getAuthorities(), "ROLE_USER");

    }

    private void assertRole(Collection<? extends GrantedAuthority> authorities,
                            String role) {
        authorities.
                stream().
                filter(a -> role.equals(a.getAuthority())).
                findAny().
                orElseThrow(() -> new AssertionFailedError("Role " + role + " not found!"));
    }

}
