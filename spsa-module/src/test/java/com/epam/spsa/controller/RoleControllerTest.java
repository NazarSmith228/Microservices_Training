package com.epam.spsa.controller;

import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.service.RoleService;
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

import java.util.Collections;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class)
@PropertySource("classpath:/exceptionMessages.properties")
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoleService roleService;

    @Value("${sportType.exception.notfound}")
    private String sportTypeExceptionMessage;

    @Test
    public void getByIdTest() throws Exception {
        RoleDto roleDto = getRoleDto();

        when(roleService.getById(anyInt()))
                .thenReturn(roleDto);

        mockMvc.perform(get("/api/v1/roles/{id}", roleDto.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(roleDto.getId()))
                .andExpect(jsonPath("$.name").value(roleDto.getName()));
    }

    @Test
    public void getByIncorrectIdTest() throws Exception {
        int id = -1111;

        when(roleService.getById(Mockito.anyInt()))
                .thenThrow(EntityNotFoundException.class);

        mockMvc.perform(get("/api/v1/roles/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getAllTest() throws Exception {
        RoleDto roleDto = getRoleDto();
        when(roleService.getAll()).thenReturn(Collections.singletonList(roleDto));

        mockMvc.perform(get("/api/v1/roles"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(roleDto.getId()))
                .andExpect(jsonPath("$.[0].name").value(roleDto.getName()));
    }

    private RoleDto getRoleDto() {
        return RoleDto.builder()
                .id(1)
                .name("COACH")
                .build();
    }

}
