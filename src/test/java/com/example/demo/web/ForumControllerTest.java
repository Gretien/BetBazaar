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
public class ForumControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testUserProfileGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/add"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("post-add"));
    }


    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testAllPostsGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/posts/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.view().name("post-all"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("posts"))
                .andExpect(MockMvcResultMatchers.model().attributeExists("username"));
    }

    @Test
    @WithUserDetails(
            value = "test",
            setupBefore = TestExecutionEvent.TEST_EXECUTION
    )
    void testPostsAllPostFail() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/posts/add")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .param("text", ""))
                .andExpect(MockMvcResultMatchers.status().is3xxRedirection())
                .andExpect(MockMvcResultMatchers.redirectedUrl("/posts/add"));
    }
}
