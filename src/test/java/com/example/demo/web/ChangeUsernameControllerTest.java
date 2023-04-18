package com.example.demo.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class ChangeUsernameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testChangeUsernameGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/change/username"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("change-username"));
    }

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testChangeUsernamePostFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/change/username")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("username","Test"))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/change/username"));
    }
}
