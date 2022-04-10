package com.epam.slsa.error;

import com.epam.slsa.controller.CoachController;
import com.epam.slsa.controller.LocationScheduleController;
import com.epam.slsa.dao.*;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.SportType;
import com.epam.slsa.service.CoachService;
import com.epam.slsa.service.LocationScheduleService;
import com.epam.slsa.validation.validators.SportTypeSubsetValidator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import javax.validation.ConstraintViolationException;
import java.util.*;
import java.util.stream.Collectors;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ComponentScan(basePackageClasses = {SportTypeDao.class},
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                classes = {AddressDao.class, CoachDao.class, LocationDao.class, CommentDao.class,
                        LocationTypeDao.class, LocationScheduleDao.class,
                        ImageDao.class, LinkDao.class}))
@WebMvcTest(controllers = {CoachController.class, LocationScheduleController.class})
@AutoConfigureMockMvc(addFilters = false)
public class ConstraintViolationExceptionTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CoachService coachService;

    @MockBean
    private LocationScheduleService locationScheduleService;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void saveCoachWithValidationExceptionTest() throws Exception {
        int locationId = 1;
        SportTypeDto sportTypeDto1 = SportTypeDto.builder().id(1).name("Runing").build();
        SportTypeDto sportTypeDto2 = SportTypeDto.builder().id(2).name("Swimming").build();
        Set<SportTypeDto> sportTypeDtoSet = new HashSet<>(Arrays.asList(sportTypeDto1, sportTypeDto2));
        List<SportType> sportTypeList1 = sportTypeDtoSet.stream().map(sport -> modelMapper.map(sport, SportType.class)).collect(Collectors.toList());

        SportType sportType1 = new SportType(1, "Running", null, null);
        SportType sportType2 = new SportType(2, "Swimming", null, null);
        List<SportType> sportTypeList2 = new ArrayList<>(Arrays.asList(sportType1, sportType2));

        CoachDto coachDto = CoachDto.builder()
                .rating(-2)
                .workWithChildren(true)
                .sportTypes(sportTypeDtoSet)
                .build();

        when(sportTypeDao.getAll()).thenReturn(sportTypeList2);
        when(coachService.save(coachDto, locationId)).thenThrow(ConstraintViolationException.class);
        when(modelMapper.map(sportTypeDtoSet, new TypeToken<List<SportType>>() {
        }.getType())).thenReturn(sportTypeList1);
        new SportTypeSubsetValidator(sportTypeDao, modelMapper);

        mockMvc.perform(post("/api/v1/locations/{id}/set/coach", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(coachDto))
                .characterEncoding("UTF-8"))
                .andDo(print())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Validation error!"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='sportTypes')].message").value("This sport types is not supported"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='rating')].message").value("must be greater than or equal to 0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveWithValidationExceptionTest() throws Exception {
        int locationId = 1;
        LocationScheduleDto locationScheduleDto1 = LocationScheduleDto.builder().day("monday").startTime("10:00").endTime("18:00").build();
        LocationScheduleDto locationScheduleDto2 = LocationScheduleDto.builder().day("tusday").startTime("10:00").endTime("18:00").build();
        LocationScheduleDto locationScheduleDto3 = LocationScheduleDto.builder().day("wednesday").startTime("110:00").endTime("18:00").build();

        Set<LocationScheduleDto> locationScheduleDtoSet = new HashSet<>(Arrays.asList(locationScheduleDto1, locationScheduleDto2, locationScheduleDto3));
        when(locationScheduleService.save(locationScheduleDtoSet, locationId)).thenThrow(ConstraintViolationException.class);

        mockMvc.perform(post("/api/v1/locations/{id}/locationSchedule", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(locationScheduleDtoSet))
                .characterEncoding("UTF-8"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.subErrors[?(@.field=='save.locationScheduleDto[].day')].message").value("This day does't exist"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='save.locationScheduleDto[].day')].rejectedValue").value(locationScheduleDto2.getDay()))
                .andExpect(jsonPath("$.subErrors[?(@.field=='save.locationScheduleDto[].startTime')].message").value("Time format is incorrect"))
                .andExpect(jsonPath("$.subErrors[?(@.field=='save.locationScheduleDto[].startTime')].rejectedValue").value(locationScheduleDto3.getStartTime()))
                .andExpect(status().isBadRequest());
    }

}
