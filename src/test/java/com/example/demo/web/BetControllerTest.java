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
public class BetControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testBetsGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/bets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("bets"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bets"));
    }



}
