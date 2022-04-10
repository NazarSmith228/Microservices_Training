package com.epam.slsa.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SwaggerController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SwaggerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void redirectTest() throws Exception {
        mockMvc.perform(get("")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is3xxRedirection());
    }

}
