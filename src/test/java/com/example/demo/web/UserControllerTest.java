package com.example.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.servlet.FlashMap;

import static org.mockito.Mockito.verify;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testRegistration() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "testov")
                        .param("firstName", "Test")
                        .param("lastName", "Testov")
                        .param("age", "20")
                        .param("password", "12345")
                        .param("confirmPassword", "12345"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    void testRegistrationFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username", "test")
                        .param("firstName", "Test")
                        .param("lastName", "Testov")
                        .param("age", "10")
                        .param("password", "12345")
                        .param("confirmPassword", "12345"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/users/register"));
    }

    @Test
    void testRegistrationGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/register"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("auth-register"));
    }

    @Test
    void testLoginGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users/login"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("auth-login"));
    }

}