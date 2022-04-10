package com.epam.spsa.security;

import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.JwtService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:/messages.properties")
public class JwtAuthenticationFilterTest {

    @InjectMocks
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Mock
    private AuthenticationProvider authenticationProvider;

    @Mock
    private JwtService jwtService;

    private static MockHttpServletRequest request;

    private static MockHttpServletResponse response;

    private static MockFilterChain filterChain;

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    @BeforeAll
    public static void classSetUp() {
        request = spy(new MockHttpServletRequest());
        response = new MockHttpServletResponse();
    }

    @BeforeEach
    public void methodSetUp() {
        jwtAuthenticationFilter.setJwtTokenCookieName(jwtTokenCookieName);
        jwtAuthenticationFilter.setJwtService(jwtService);
        filterChain = spy(MockFilterChain.class);
    }

    @Test
    public void resumeFilterChainIfTokenNull() throws IOException, ServletException {
        jwtAuthenticationFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    public void deleteJwtTokenIfNotValid() throws IOException, ServletException {
        Cookie jwtCookie = mock(Cookie.class);

        when(jwtCookie.getValue()).thenReturn("test");
        when(jwtCookie.getName()).thenReturn(jwtTokenCookieName);
        when(jwtService.validateToken(jwtCookie.getValue())).thenReturn(false);
        when(request.getCookies()).thenReturn(new Cookie[] { jwtCookie });

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(jwtCookie).setPath("/");
        verify(jwtCookie).setMaxAge(0);
        verify(jwtCookie).setValue("");
    }

    @Test
    public void successAuthentication() throws IOException, ServletException {
        String token = "test";

        when(jwtService.validateToken(any())).thenReturn(true);
        when(request.getCookies()).thenReturn(new Cookie[] { new Cookie(jwtTokenCookieName, token) });
        when(authenticationProvider.authenticate(any())).thenReturn(getAuthentication(token));

        jwtAuthenticationFilter.doFilter(request, response, filterChain);

        verify(authenticationProvider).authenticate(any());
    }

    private static Authentication getAuthentication(String token) {
        JwtAuthenticationToken jwtAuthenticationToken = new JwtAuthenticationToken(token);
        jwtAuthenticationToken.setUserPrincipal(new UserPrincipal());
        return jwtAuthenticationToken;
    }

}
