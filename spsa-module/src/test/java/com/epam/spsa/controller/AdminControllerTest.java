package com.epam.spsa.controller;

import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AdminController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SlsaClient slsaClient;

    @Test
    public void successfulGetLocations() throws Exception {
        when(slsaClient.getByAdminId(anyInt())).thenReturn(getMainLocationsDto());
        mockMvc.perform(get("/api/v1/admin/{adminId}/locations", 1))
                .andExpect(status().isOk());
    }

    private static List<MainLocationDto> getMainLocationsDto() {
        List<MainLocationDto> locationDtos = new ArrayList<>();
        locationDtos.add(MainLocationDto.builder().build());
        return locationDtos;
    }

}
