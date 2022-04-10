package com.epam.spsa.controller;

import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.User;
import com.epam.spsa.service.EventService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventController.class)
@AutoConfigureMockMvc(addFilters = false)
@PropertySource("classpath:/exceptionMessages.properties")
@ActiveProfiles("default")
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EventService eventService;

    @MockBean
    private UserDao userDao;

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${event.exception.notfound}")
    private String eventNotFoundExceptionMessage;

    @Value("${user.exception.notfound}")
    private String userExceptionMessage;

    @Test
    void createValidEventTest() throws Exception {
        when(eventService.save(any(EventDto.class))).thenReturn(getMainEventDto());
        when(userDao.getById(anyInt())).thenReturn(new User());

        mockMvc.perform(post("/api/v1/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getEventDto()))
                .characterEncoding("UTF-8"))
                .andExpect(status().isCreated());
    }

    @Test
    void createEventWithIncorrectTimeTest() throws Exception {
        when(eventService.save(any(EventDto.class))).thenReturn(getMainEventDto());
        when(userDao.getById(anyInt())).thenReturn(new User());

        EventDto eventDto = getEventDto();
        eventDto.setDate("100000:1000:10000");

        mockMvc.perform(post("/api/v1/events/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(eventDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getEventWithIdTest() throws Exception {
        MainEventDto mainEventDto = getMainEventDto();
        when(eventService.getById(anyInt())).thenReturn(mainEventDto);

        mockMvc.perform(get("/api/v1/events/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(mainEventDto.getId()))
                .andExpect(jsonPath("$.date").value(mainEventDto.getDate()))
                .andExpect(jsonPath("$.time").value(mainEventDto.getTime()))
                .andExpect(jsonPath("$.owner_id").value(mainEventDto.getOwner_id()))
                .andExpect(jsonPath("$.description").value(mainEventDto.getDescription()))
                .andExpect(jsonPath("$.location_id").value(mainEventDto.getLocation_id()));
    }

    @Test
    void getEventWithIncorrectIdTest() throws Exception {
        int id = -1;
        when(eventService.getById(id))
                .thenThrow(new EntityNotFoundException(eventNotFoundExceptionMessage + id));

        mockMvc.perform(get("/api/v1/events/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(eventNotFoundExceptionMessage + id));
    }


    @Test
    void getEventWithUserIdTest() throws Exception {
        when(eventService.getByUserId(anyInt()))
                .thenReturn(Collections.singletonList(getMainEventDto()));

        mockMvc.perform(get("/api/v1/events/user/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    void getEventWithIncorrectUserIdTest() throws Exception {
        int id = -1;
        when(eventService.getById(id))
                .thenThrow(new EntityNotFoundException(userExceptionMessage + id));

        mockMvc.perform(get("/api/v1/events/{id}", id))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(userExceptionMessage + id));
    }

    @Test
    void getEventsTest() throws Exception {
        when(eventService.getByUserId(anyInt()))
                .thenReturn(Collections.singletonList(getMainEventDto()));

        mockMvc.perform(get("/api/v1/events/"))
                .andExpect(status().isOk());
    }

    @Test
    void updateValidEventByIdTest() throws Exception {
        int id = 1;

        MainEventDto mainEventDto = getMainEventDto();
        when(eventService.update(any(EventDto.class), anyInt()))
                .thenReturn(mainEventDto);
        when(userDao.getById(anyInt())).thenReturn(new User());

        mockMvc.perform(put("/api/v1/events/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getEventDto()))
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());

    }

    @Test
    void updateNotValidEventByIdTest() throws Exception {
        int id = 1;

        MainEventDto mainEventDto = getMainEventDto();
        when(eventService.update(any(EventDto.class), anyInt()))
                .thenReturn(mainEventDto);
        when(userDao.getById(anyInt())).thenReturn(new User());

        mockMvc.perform(put("/api/v1/events/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new EventDto()))
                .characterEncoding("UTF-8"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateValidEventByIncorrectIdTest() throws Exception {
        int id = -1;

        when(eventService.update(any(EventDto.class), anyInt()))
                .thenThrow(new EntityNotFoundException(eventNotFoundExceptionMessage + id));
        when(userDao.getById(anyInt())).thenReturn(new User());

        mockMvc.perform(put("/api/v1/events/{id}", id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getEventDto())))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value(eventNotFoundExceptionMessage + id));
    }

    @Test
    void deleteEventByIdTest() throws Exception {

        doNothing().when(eventService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/events/{id}", 1))
                .andExpect(status().isOk());

    }

    @Test
    void deleteEventByIncorrectIdTest() throws Exception {
        int id = -1;
        doThrow(new EntityNotFoundException(eventNotFoundExceptionMessage + id))
                .when(eventService).delete(id);

        mockMvc.perform(delete("/api/v1/events/{id}", id))
                .andExpect(status().isNotFound());

    }


    private EventDto getEventDto() {
        return EventDto.builder()
                .date("2020-04-14")
                .description("Sport event")
                .location_id(4)
                .owner_id(1)
                .time("10:10:10")
                .userIdList(new HashSet<>(Arrays.asList(2, 3)))
                .build();
    }

    private MainEventDto getMainEventDto() {
        return MainEventDto.builder()
                .id(1)
                .date("2020-04-14")
                .description("Sport event")
                .location_id(4)
                .owner_id(1)
                .userIdList(Arrays.asList(2, 3))
                .time("10:10:10")
                .build();
    }

}