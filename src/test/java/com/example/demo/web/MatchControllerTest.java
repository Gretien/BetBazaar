package com.example.demo.web;

import com.example.demo.domain.models.binding.AddBetModel;
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

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@AutoConfigureMockMvc
public class MatchControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testMatchesGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/matches"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("matches"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("bets"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("totalOdds"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("matches"));
    }

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testMatchesPostFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/matches")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("price", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/matches"));
    }




}
