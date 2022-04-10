package com.epam.slsa.controller;

import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.dto.locationSchedule.MainLocationScheduleDto;
import com.epam.slsa.service.LocationScheduleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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

import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = LocationScheduleController.class)
@AutoConfigureMockMvc(addFilters = false)
public class LocationScheduleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LocationScheduleService locationScheduleService;

    @Autowired
    private ObjectMapper objectMapper;

    private Set<LocationScheduleDto> locationScheduleDtos;

    private Set<MainLocationScheduleDto> mainLocationSchedulesDtos;

    @BeforeEach
    public void setUpMockito() {
        Mockito.when(locationScheduleService.save(ArgumentMatchers.anySet(), anyInt()))
                .thenReturn(mainLocationSchedulesDtos);
        Mockito.when(locationScheduleService.update(ArgumentMatchers.anySet(), anyInt()))
                .thenReturn(locationScheduleDtos);
        Mockito.when(locationScheduleService.getAllByLocationId(5))
                .thenReturn(mainLocationSchedulesDtos);
    }

    @BeforeEach
    public void setUpVariables() {
        LocationScheduleDto locationScheduleDto = LocationScheduleDto.builder()
                .day("Monday")
                .startTime("09:12:12")
                .endTime("22:22:22")
                .build();
        locationScheduleDtos = new HashSet<>();
        locationScheduleDtos.add(locationScheduleDto);

        MainLocationScheduleDto mainLocationScheduleDto = MainLocationScheduleDto.builder()
                .id(1)
                .day("Monday")
                .startTime("09:12:12")
                .endTime("22:22:22")
                .build();
        mainLocationSchedulesDtos = new HashSet<>();
        mainLocationSchedulesDtos.add(mainLocationScheduleDto);
    }

    @Test
    public void testSaveLocationSchedule() throws Exception {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        mockMvc.perform(post("/api/v1/locations/{id}/locationSchedule", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationScheduleDtos)))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].day").value("Monday"))
                .andExpect(jsonPath("$.[0].startTime").value("09:12:12"))
                .andExpect(jsonPath("$.[0].endTime").value("22:22:22"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testUpdateLocationSchedule() throws Exception {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        mockMvc.perform(put("/api/v1/locations/{id}/locationSchedule", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationScheduleDtos)))
                .andExpect(jsonPath("$.[0].day").value("Monday"))
                .andExpect(jsonPath("$.[0].startTime").value("09:12:12"))
                .andExpect(jsonPath("$.[0].endTime").value("22:22:22"))
                .andExpect(status().isOk());
    }

    @Test
    public void testGetLocationSchedule() throws Exception {
        mockMvc.perform(get("/api/v1/locations/{id}/locationSchedule", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].day").value("Monday"))
                .andExpect(jsonPath("$.[0].startTime").value("09:12:12"))
                .andExpect(jsonPath("$.[0].endTime").value("22:22:22"))
                .andExpect(status().isOk());
    }

}

