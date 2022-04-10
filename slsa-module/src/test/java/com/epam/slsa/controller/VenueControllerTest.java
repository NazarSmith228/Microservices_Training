package com.epam.slsa.controller;

import com.epam.slsa.builders.criteria.CriteriaBuilder;
import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.service.VenueService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;

import static com.epam.slsa.builders.criteria.CriteriaInfo.id;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getCriteriaLocationDto;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getMainLocationDto;
import static com.epam.slsa.builders.sportType.SportTypeBuilder.getSportType;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = VenueController.class)
@AutoConfigureMockMvc(addFilters = false)
public class VenueControllerTest {

    @MockBean
    private VenueService venueService;
    @MockBean
    private SportTypeDao sportTypeDao;
    @MockBean
    private ModelMapper modelMapper;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    private CriteriaDto criteriaDto;

    @BeforeEach
    void setUpMockito() {
        Mockito.when(modelMapper.map(Mockito.anySet(), Mockito.any(Type.class)))
                .thenReturn(Lists.newArrayList(getSportType()));
        Mockito.when(venueService.getSuitableLocation(Mockito.any(CriteriaDto.class)))
                .thenReturn(Lists.newArrayList(getCriteriaLocationDto()));
        Mockito.when(sportTypeDao.getAll())
                .thenReturn(Lists.newArrayList(getSportType()));
    }

    @BeforeEach
    void setUpVariables() {
        criteriaDto = CriteriaBuilder.getCriteriaDto();
    }

    @Test
    public void matchTest() throws Exception {
        mockMvc.perform(post("/api/v1/locations/criteria/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteriaDto)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().json("[{'id':5,'name':'Fun club','address':{'latitude':12.5," +
                        "'longitude':120.0},'locationType':{'id':4,'name':'Studio','placing':Indoor}," +
                        "'locationSchedule':null,'coaches':[{'id':5,'rating':5," +
                        "'workWithChildren':true,'userId':1,'sportTypes':[{'id':1,'name':" +
                        "'Running'}]}],'sportTypes':[{'id':2,'name':'Football'}]}]"));
    }

    @Test
    public void matchTestWithIncorrectLatitude() throws Exception {
        criteriaDto.setLatitude(1292968);
        mockMvc.perform(post("/api/v1/locations/criteria/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteriaDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void matchTestWithIncorrectLongitude() throws Exception {
        criteriaDto.setLongitude(1292968);
        mockMvc.perform(post("/api/v1/locations/criteria/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteriaDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void matchTestWithIncorrectPlacing() throws Exception {
        criteriaDto.setPlacing("Kardan");
        mockMvc.perform(post("/api/v1/locations/criteria/venues")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(criteriaDto)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}










