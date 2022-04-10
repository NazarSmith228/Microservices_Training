package com.epam.slsa.controller;

import com.epam.slsa.builders.sportType.SportTypeBuilder;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.service.SportTypeService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SportTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class SportTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportTypeService sportTypeService;

    private int sportTypeId;
    private int incorrectSportTypeId;
    private String sportType;
    private SportTypeDto expectedSportTypeDto;

    @BeforeEach
    public void setUpVariables() {
        sportTypeId = 2;
        incorrectSportTypeId = -1;
        sportType = "Swimming";
        expectedSportTypeDto = SportTypeBuilder.getSportTypeDto();

    }

    @BeforeEach
    public void setUpMockito() {
        Mockito.when(sportTypeService.getById(ArgumentMatchers.eq(sportTypeId))).thenReturn(expectedSportTypeDto);
        Mockito.when(sportTypeService.getById(ArgumentMatchers.eq(incorrectSportTypeId))).thenThrow(EntityNotFoundException.class);
        Mockito.when(sportTypeService.getAll()).thenReturn(Lists.newArrayList(expectedSportTypeDto));
    }

    @Test
    public void getById() throws Exception {
        mockMvc.perform(get("/api/v1/sportTypes/{id}", sportTypeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(sportType))
                .andExpect(jsonPath("$.id").value(sportTypeId));
    }

    @Test()
    public void getByInvalidId() throws Exception {
        mockMvc.perform(get("/api/v1/sportTypes/{id}", incorrectSportTypeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/sportTypes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(sportTypeId))
                .andExpect(jsonPath("$.[0].name").value(sportType))
                .andExpect(status().isOk());
    }

}
