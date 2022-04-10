package com.epam.slsa.controller;

import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.service.LocationTypeService;
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

@WebMvcTest(LocationTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LocationTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationTypeService sportTypeService;

    private int locationTypeId;
    private String locationType;
    private LocationTypeDto expectedLocationTypeDto;

    @BeforeEach
    public void setUpVariables() {
        locationTypeId = 1;
        locationType = "Studio";
        expectedLocationTypeDto = LocationTypeDto.builder()
                .id(locationTypeId)
                .name(locationType)
                .build();
    }

    @BeforeEach
    public void setUpMockito() {
        Mockito.when(sportTypeService.getById(ArgumentMatchers.eq(locationTypeId))).thenReturn(expectedLocationTypeDto);
        Mockito.when(sportTypeService.getAll()).thenReturn(Lists.newArrayList(expectedLocationTypeDto));
    }

    @Test()
    public void getById() throws Exception {
        mockMvc.perform(get("/api/v1/locationTypes/{id}", locationTypeId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(locationType))
                .andExpect(jsonPath("$.id").value(locationTypeId));
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(get("/api/v1/locationTypes")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(locationTypeId))
                .andExpect(jsonPath("$.[0].name").value(locationType))
                .andExpect(status().isOk());
    }

}
