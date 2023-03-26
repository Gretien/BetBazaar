package com.example.demo.services.userDetails;

import com.example.demo.domain.entities.Role;
import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.repositories.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public class ApplicationUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public ApplicationUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return
                userRepository.
                        findByUsername(username).
                        map(this::map).
                        orElseThrow(() -> new UsernameNotFoundException("User with name " + username + " not found!"));
    }

    public UserDetails map(com.example.demo.domain.entities.User userEntity) {
        return new AppUserDetails(
                userEntity.getUsername(),
                userEntity.getPassword(),
                extractAuthorities(userEntity)
        ).setAge(userEntity.getAge())
                .setFirstName(userEntity.getFirstName())
                .setLastName(userEntity.getLastName())
                .setBalance(userEntity.getBalance())
                .setBets(userEntity.getBets())
                .setPosts(userEntity.getPosts());
    }

    private List<GrantedAuthority> extractAuthorities(com.example.demo.domain.entities.User userEntity) {
        return userEntity.
                getRoles().
                stream().
                map(this::mapRole).
                toList();
    }

    private GrantedAuthority mapRole(Role userRoleEntity) {
        return new SimpleGrantedAuthority("ROLE_" + userRoleEntity.getRoleType().name());
    }

}
