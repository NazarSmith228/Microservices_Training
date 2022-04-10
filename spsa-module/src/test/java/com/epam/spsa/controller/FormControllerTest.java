package com.epam.spsa.controller;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.error.exception.AccessDeniedException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.Role;
import com.epam.spsa.service.FormService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = FormController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class FormControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FormService formService;

    @MockBean
    private RoleDao roleDao;

    @MockBean
    private ModelMapper mapper;

    @Test
    public void successfulSaveTest() throws Exception {
        when(formService.save(any())).thenReturn(1);
        when(roleDao.getAll()).thenReturn(getRoles());
        when(mapper.map(any(), eq(Role.class))).thenReturn(getCoachRole());

        mockMvc.perform(post("/api/v1/forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getFormDto())))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void locationNotFoundSaveTest() throws Exception {
        when(formService.save(any())).thenThrow(EntityNotFoundException.class);
        when(roleDao.getAll()).thenReturn(getRoles());
        when(mapper.map(any(), eq(Role.class))).thenReturn(getCoachRole());

        mockMvc.perform(post("/api/v1/forms")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getFormDto())))
                .andExpect(status().isNotFound());
    }

    @Test
    public void successfulApproveTest() throws Exception {
        doNothing().when(formService).approve(anyInt());

        mockMvc.perform(get("/api/v1/forms/approve/{id}", 1))
                .andExpect(status().isOk());
    }

    @Test
    public void formNotFoundApproveTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(formService).approve(anyInt());

        mockMvc.perform(get("/api/v1/forms/approve/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void successfulGetByIdTest() throws Exception {
        when(formService.getById(anyInt())).thenReturn(getMainFormDto());

        mockMvc.perform(get("/api/v1/forms/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void formNotFoundGetByIdTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(formService).getById(anyInt());

        mockMvc.perform(get("/api/v1/forms/{id}", 1))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteNotExistingFormTest() throws Exception {
        doThrow(AccessDeniedException.class).when(formService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/forms/{id}", 52))
                .andExpect(status().isForbidden());
    }

    @Test
    public void deleteAccessDeniedTest() throws Exception {
        doThrow(EntityNotFoundException.class).when(formService).delete(anyInt());

        mockMvc.perform(delete("/api/v1/forms/{id}", 52))
                .andExpect(status().isNotFound());
    }

    private static MainFormDto getMainFormDto() {
        return MainFormDto.builder()
                .id(1)
                .locationName("Test Location")
                .address(getAddressDto())
                .role(getCoachRoleDto())
                .build();
    }

    private static FormDto getFormDto() {
        return FormDto.builder()
                .locationName("Test Location")
                .address(getAddressDto())
                .role(getCoachRoleDto())
                .build();
    }

    private static RoleDto getCoachRoleDto() {
        return RoleDto.builder()
                .id(2)
                .name("COACH")
                .build();
    }

    private static Role getCoachRole() {
        return Role.builder()
                .id(2)
                .name("COACH")
                .build();
    }

    private static List<Role> getRoles() {
        return Collections.singletonList(getCoachRole());
    }

    private static AddressDto getAddressDto() {
        return AddressDto.builder()
                .latitude(10.0)
                .longitude(20.0)
                .build();
    }

}
