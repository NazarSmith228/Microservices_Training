package com.epam.spsa.controller;

import com.epam.spsa.controller.builder.SportTypeDtoBuilder;
import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.dao.SportTypeDao;
import com.epam.spsa.dto.user.CommonUserStatDto;
import com.epam.spsa.dto.user.MainCommonUserStatDto;
import com.epam.spsa.dto.user.MainUserStatsDto;
import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.model.SportType;
import com.epam.spsa.service.UserStatsService;
import com.epam.spsa.validation.validators.commonUserStatDto.CoachesIdSetValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.netflix.ribbon.RibbonAutoConfiguration;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.cloud.openfeign.FeignContext;
import org.springframework.cloud.openfeign.ribbon.FeignRibbonClientAutoConfiguration;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(controllers = {UserStatsController.class, FeignContext.class})
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
@ImportAutoConfiguration({FeignAutoConfiguration.class})
class UserStatsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserStatsService userStatsService;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @MockBean
    private CoachesIdSetValidator coachesIdSetValidator;

    UserStatsControllerTest() {
    }

    @Test
    public void saveUserStatsTest() throws Exception {
        UserStatsDto dto = UserBuilder.getUserStatsDto();
        MainUserStatsDto mainDto = UserBuilder.getMainUserStatsDto();

        SportType sportType = SportTypeDtoBuilder.getSportType();

        Mockito.when(userStatsService.saveUserStats(dto, 1)).thenReturn(mainDto);

        mockMvc.perform(post("/api/v1/user/1/stats")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getAllStatsTest() throws Exception {
        when(userStatsService.getAllUserStats(1)).thenReturn(Arrays.asList(UserBuilder.getMainUserStatsDto()));
        mockMvc.perform(get("/api/v1/user/1/stats")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(UserBuilder.getUserStats())))
                .andExpect(status().isOk())
                .andExpect(content()
                        .string(objectMapper.writeValueAsString(Arrays.asList(UserBuilder.getMainUserStatsDto()))))
                .andReturn();

    }

    @Test
    public void updateLastStatsTest() throws Exception {
        UserStatsDto dto = UserBuilder.getUserStatsDto();
        MainUserStatsDto mainDto = UserBuilder.getMainUserStatsDto();

        SportType sportType = SportTypeDtoBuilder.getSportType();

        Mockito.when(userStatsService.saveUserStats(dto, 1)).thenReturn(mainDto);

        mockMvc.perform(put("/api/v1/user/1/stats")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void getLastStats() throws Exception {
        UserStatsDto dto = UserBuilder.getUserStatsDto();
        MainUserStatsDto mainDto = UserBuilder.getMainUserStatsDto();

        Mockito.when(userStatsService.getLastUserStats(1)).thenReturn(mainDto);

        mockMvc.perform(get("/api/v1/user/1/stats/last")
                .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(content().string(objectMapper.writeValueAsString(mainDto)));
    }

    @Test
    public void getCommonUserStat() throws Exception {
        int userID = 1;
        CommonUserStatDto commonUserStatDto = UserBuilder.getCommonStatDto();

        Mockito.when(userStatsService.getCommonStat(commonUserStatDto, userID))
                .thenAnswer((Answer<MainCommonUserStatDto>) invocationOnMock -> UserBuilder.getMainCommonStatDto());


        mockMvc.perform(post("/api/v1/user/{id}/common-stats", userID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(commonUserStatDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultH").value("PT17H3M12S"))
                .andExpect(jsonPath("$.resultKm").value(25));
    }

    @Test
    public void getCommonUserStatIncorrect() throws Exception {
        int userID = 1;
        CommonUserStatDto commonUserStatDto = UserBuilder.getCommonStatDto();
        commonUserStatDto.setStartOfInterval(LocalDate.parse("2021-04-21"));

        Mockito.when(userStatsService.getCommonStat(commonUserStatDto, userID))
                .thenAnswer((Answer<MainCommonUserStatDto>) invocationOnMock -> UserBuilder.getMainCommonStatDto());

        mockMvc.perform(post("/api/v1/user/{id}/common-stats", userID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(commonUserStatDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }
}