package com.epam.spsa.controller;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.service.SecurityUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RegistrationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SecurityUserService securityUserService;

    @MockBean
    private JwtService jwtService;

    @Value("${jwt.token.incorrect}")
    private String incorrectTokenExceptionMessage;

    @Test
    public void registerUserTest() throws Exception {
        mockMvc.perform(post("/register")
                .header("Origin", "test")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getAuthDto())))
                .andExpect(status().isOk());

        verify(securityUserService).register(any());
    }

    @Test
    public void shouldReturnJwtExceptionIfActivationTokenIsEmpty() throws Exception {
        when(jwtService.getSubject(any())).thenThrow(JwtException.class);

        mockMvc.perform(get("/activate-user")
                .queryParam("token", "")
                .header("Origin", "test"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(incorrectTokenExceptionMessage));

        verify(securityUserService, times(0)).activate(any(), any());
    }

    private static AuthDto getAuthDto() {
        return AuthDto.builder()
                .email("test@gmail.com")
                .password("testTest0")
                .build();
    }

}
