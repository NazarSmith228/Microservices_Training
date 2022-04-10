package com.epam.spsa.controller;

import com.epam.spsa.dao.SportTypeDao;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.SportType;
import com.epam.spsa.service.PartnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static com.epam.spsa.controller.builder.CriteriaDtoBuilder.*;
import static com.epam.spsa.controller.builder.UserBuilder.getCriteriaUserDto;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PartnerController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class PartnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PartnerService partnerService;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @Value("${criteria.user.exception.notfound}")
    private String criteriaUserExceptionMessage;

    @Value("${criteria.exception.notfound}")
    private String criteriaExceptionMessage;

    @BeforeEach
    public void setUp() {
        when(modelMapper.map(any(SportTypeDto.class), any()))
                .thenReturn(SportType.builder()
                        .id(5)
                        .name("Running")
                        .build());

        List<SportType> allSportTypes = Lists.list(SportType.builder()
                .id(5)
                .name("Running")
                .build());

        PaginationDto<CriteriaUserDto> allPartnersDto = PaginationDto.<CriteriaUserDto>builder()
                .entities(Lists.newArrayList(getCriteriaUserDto()))
                .quantity(1)
                .entitiesLeft(0)
                .build();

        when(sportTypeDao.getAll()).thenReturn(allSportTypes);

        when(partnerService.getSuitablePartner(any(CriteriaDto.class), anyInt(), anyInt(), anyInt()))
                .thenReturn(allPartnersDto);
    }

    @Test
    public void matchTest() throws Exception {
        int userId = 5;
        String pageNumber = "1";
        String pageSize = "3";

        mockMvc.perform(post("/api/v1/users/{id}/partners", userId)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCriteriaDto())));
    }

    @Test
    public void saveCriteriaTest() throws Exception {
        int userId = 4;
        int criteriaId = 1;

        when(partnerService.save(getCriteriaDto(), userId))
                .thenReturn(criteriaId);

        mockMvc.perform(post("/api/v1/users/{id}/criteria", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getCriteriaDto()))
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(criteriaId));
    }

    @Test
    public void getAllPartnersTest() throws Exception {
        List<MainCriteriaDto> mainCriteriaDtoList = new ArrayList<>(Arrays.asList(getMainCriteriaDto1(), getMainCriteriaDto2()));

        when(partnerService.getAll())
                .thenReturn(mainCriteriaDtoList);

        mockMvc.perform(get("/api/v1/users/criteria")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(mainCriteriaDtoList.size())));
    }

    @Test
    public void getCriteriaByUserIdTest() throws Exception {
        int userId = 2;
        List<MainCriteriaDto> criteriaDtoList = new ArrayList<>(Collections.singletonList(getMainCriteriaDto1()));

        when(partnerService.getCriteriaByUserId(userId))
                .thenReturn(criteriaDtoList);

        mockMvc.perform(get("/api/v1/users/{id}/criteria", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(criteriaDtoList.size())))
                .andExpect(jsonPath("$.[0].runningDistance").value(getMainCriteriaDto1().getRunningDistance()));
    }

    @Test
    public void getCriteriaByInvalidUserIdTest() throws Exception {
        int userId = -2;

        when(partnerService.getCriteriaByUserId(userId))
                .thenThrow(new EntityNotFoundException(criteriaUserExceptionMessage + userId));

        mockMvc.perform(get("/api/v1/users/{id}/criteria", userId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(criteriaUserExceptionMessage + userId));
    }

    @Test
    public void deleteCriteriaByIdTest() throws Exception {
        int userId = 1;
        int criteriaId = 2;

        doNothing().when(partnerService).delete(userId, criteriaId);

        mockMvc.perform(delete("/api/v1/users//{userId}/criteria/{criteriaId}", userId, criteriaId))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCriteriaByInvalidIdTest() throws Exception {
        int userId = 1;
        int criteriaId = -2;

        doThrow(new EntityNotFoundException(criteriaExceptionMessage + criteriaId))
                .when(partnerService).delete(userId, criteriaId);

        mockMvc.perform(delete("/api/v1/users//{userId}/criteria/{criteriaId}", userId, criteriaId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(criteriaExceptionMessage + criteriaId));
    }

}