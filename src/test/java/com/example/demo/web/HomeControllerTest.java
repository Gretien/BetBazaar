package com.example.demo.web;

import com.example.demo.domain.helpers.AppUserDetails;
import com.example.demo.domain.models.UserModel;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;


    @Test
    public void testGetHomeWithUnauthenticatedUser() throws Exception {

        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }
}
