package com.epam.slsa.controller;

import com.epam.slsa.builders.coach.CoachInfo;
import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.SportType;
import com.epam.slsa.service.CoachService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoachDto;
import static com.epam.slsa.builders.coach.CoachDtoBuilder.getMainCoachDto;
import static com.epam.slsa.builders.coach.CoachInfo.*;
import static com.epam.slsa.builders.location.LocationInfo.locationId;
import static com.epam.slsa.builders.sportType.SportTypeBuilder.getSportType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CoachController.class)
@PropertySource("classpath:/messages.properties")
@AutoConfigureMockMvc(addFilters = false)
public class CoachControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoachService coachService;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private SportType expectedSportType;

    @Value("${coach.exception.notfound}")
    private String coachExceptionMessage;

    @BeforeEach
    public void setUpVariables() {
        expectedSportType = getSportType();
    }

    @BeforeEach
    public void setUpMockito() {
        when(modelMapper.map(ArgumentMatchers.any(), any(Type.class)))
                .thenReturn(Lists.newArrayList(expectedSportType));

        when(sportTypeDao.getAll())
                .thenReturn(Lists.newArrayList(expectedSportType));

        when(coachService.save(ArgumentMatchers.any(CoachDto.class), ArgumentMatchers.anyInt()))
                .thenReturn(getMainCoachDto());

        when(coachService.update(ArgumentMatchers.any(CoachDto.class), anyInt(), anyInt()))
                .thenReturn(getMainCoachDto());

        when(coachService.getAllByLocationId(Mockito.anyInt()))
                .thenReturn(Lists.newArrayList(getMainCoachDto()));

        when(coachService.getByIdAndLocationId(
                Mockito.eq(coachId), Mockito.eq(CoachInfo.locationId)))
                .thenReturn(getMainCoachDto());

        when(coachService.getByIdAndLocationId(Mockito.eq(-1), Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

    }

    @Test
    public void getAllByLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}/coaches", CoachInfo.locationId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(CoachInfo.locationId))
                .andExpect(jsonPath("$.[0].rating").value(rating))
                .andExpect(jsonPath("$.[0].workWithChildren").value(workWithChildren))
                .andExpect(jsonPath("$.[0].sportTypes.[0].id").value(1))
                .andExpect(jsonPath("$.[0].sportTypes.[0].name").value("Running"));
    }

    @Test
    public void getByLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}/coaches/{coachId}", CoachInfo.locationId, coachId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(CoachInfo.locationId))
                .andExpect(jsonPath("$.rating").value(rating))
                .andExpect(jsonPath("$.workWithChildren").value(workWithChildren))
                .andExpect(jsonPath("$.sportTypes.[0].id").value(1))
                .andExpect(jsonPath("$.sportTypes.[0].name").value("Running"))
                .andExpect(status().isOk());
    }

    @Test
    public void getByIncorrectLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}/coaches/{coachId}",
                CoachInfo.locationId, -1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveCoach() throws Exception {
        mockMvc.perform(post("/api/v1/locations/{id}/set/coach", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCoachDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(rating))
                .andExpect(jsonPath("$.workWithChildren").value(workWithChildren))
                .andExpect(jsonPath("$.sportTypes.[0].id").value(1))
                .andExpect(jsonPath("$.sportTypes.[0].name").value("Running"));
    }

    @Test
    public void testSaveVoidCoach() throws Exception {
        mockMvc.perform(post("/api/v1/locations/{id}/set/coach", 5)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveNullCoach() throws Exception {
        CoachDto newVoidCoachDto = new CoachDto();

        mockMvc.perform(post("/api/v1/locations/{id}/set/coach", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newVoidCoachDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateCoach() throws Exception {
        mockMvc.perform(put("/api/v1/locations/{id}/coaches/{coachId}", locationId, 3)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCoachDto())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(5))
                .andExpect(jsonPath("$.workWithChildren").value(true))
                .andExpect(jsonPath("$.sportTypes.[0].id").value(1))
                .andExpect(jsonPath("$.sportTypes.[0].name").value("Running"));
    }

    @Test
    public void testDeleteCoach() throws Exception {
        int coachId = 3;

        doNothing()
                .when(coachService)
                .delete(coachId);

        mockMvc.perform(delete("/api/v1/coaches/{coachId}", coachId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCoachDto())))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteExceptionCoach() throws Exception {
        int coachId = -3;

        doThrow(new EntityNotFoundException(coachExceptionMessage + coachId))
                .when(coachService)
                .delete(coachId);

        mockMvc.perform(delete("/api/v1/coaches/{coachId}", coachId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(coachExceptionMessage + coachId))
                .andExpect(status().isNotFound());
    }


    @Test
    public void deleteLinkTest() throws Exception {
        int id = 1;
        Mockito.doNothing().when(coachService).deleteCoachLinkById(id);

        mockMvc.perform(delete("/api/v1/coaches/links/{linkId}", id))
                .andExpect(status().isOk());
    }

}

