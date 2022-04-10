package com.epam.spsa.controller;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.model.User;
import com.epam.spsa.security.JwtAuthenticationToken;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.AuthenticationService;
import com.epam.spsa.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.test.web.servlet.MockMvc;

import javax.servlet.http.Cookie;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthenticationService authenticationService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private AuthenticationProvider authenticationProvider;

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    private String token = "testJWTToken";

    @Test
    public void shouldNotAllowAccessToUsersEndpoint() throws Exception {
        mockMvc
                .perform(get("/api/v1/users/")
                        .header("Origin", "test"))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldAllowAccessToUsersEndpoint() throws Exception {
        when(jwtService.validateToken(eq(token))).thenReturn(true);
        when(jwtService.getSubject(token)).thenReturn("email");
        when(authenticationProvider.authenticate(any())).thenAnswer(invocationOnMock -> {
            JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
            jwtAuthenticationToken.setUserPrincipal(UserPrincipal.builder()
                    .user(
                            User.builder()
                                    .email("user@gmail.com")
                                    .password("testTest0")
                                    .build()
                    ).build());
            return jwtAuthenticationToken;
        });

        mockMvc
                .perform(get("/api/v1/users/")
                        .header("Origin", "test")
                        .cookie(new Cookie(jwtTokenCookieName, token)))
                .andExpect(status().isOk());
    }

    @Test
    public void shouldReturnJwtAuthenticationToken() throws Exception {
        AuthDto user = AuthDto.builder()
                .email("test@gmail.com")
                .password("testTest0")
                .build();

        when(authenticationService.authenticate(user)).thenReturn(token);

        mockMvc.perform(post("/authenticate")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Origin", "test")
                .content(objectMapper.writeValueAsString(user)))
                .andExpect(status().isOk())
                .andExpect(cookie().value(jwtTokenCookieName, token));
    }

}
