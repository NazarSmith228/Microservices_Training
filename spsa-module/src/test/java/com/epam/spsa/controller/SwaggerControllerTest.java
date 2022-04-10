package com.epam.spsa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SwaggerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class SwaggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void redirectionTest() throws Exception {
        mockMvc.perform(get("", "/swagger"))
                .andExpect(status().is3xxRedirection());
    }

}