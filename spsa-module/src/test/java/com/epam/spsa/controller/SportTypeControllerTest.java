package com.epam.spsa.controller;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.service.SportTypeService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SportTypeController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class SportTypeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SportTypeService sportTypeService;

    @Value("${sportType.exception.notfound}")
    private String sportTypeExceptionMessage;

    @Test
    public void getByIdTest() throws Exception {
        SportTypeDto sportTypeDto = SportTypeDto.builder()
                .id(2)
                .name("Yoga")
                .build();

        when(sportTypeService.getSportTypeById(eq(5)))
                .thenReturn(sportTypeDto);

        mockMvc.perform(get("/api/v1/sportTypes/{id}", 5))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.name").value("Yoga"));
    }

    @Test
    public void getSportTypeByIncorrectId() throws Exception {
        int id = -1111;

        when(sportTypeService.getSportTypeById(Mockito.anyInt()))
                .thenThrow(new EntityNotFoundException(sportTypeExceptionMessage + id));

        mockMvc.perform(get("/api/v1/sportTypes/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(sportTypeExceptionMessage + id));
    }

    @Test
    public void getAllTest() throws Exception {
        SportTypeDto sportTypeDto = SportTypeDto.builder().id(2).name("Yoga").build();
        when(sportTypeService.getAllSportTypes()).thenReturn(Lists.list(sportTypeDto));

        mockMvc.perform(get("/api/v1/sportTypes/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(2))
                .andExpect(jsonPath("$.[0].name").value("Yoga"));
    }

}