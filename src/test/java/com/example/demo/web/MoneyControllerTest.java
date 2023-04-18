package com.example.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class MoneyControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testDepositGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/deposit"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("deposit"));
    }

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testDepositPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/deposit")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("amount", "10"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testWithdrawGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/withdraw"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("withdraw"));
    }

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testWithdrawPost() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/withdraw")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("amount", "10"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/"));
    }
}
