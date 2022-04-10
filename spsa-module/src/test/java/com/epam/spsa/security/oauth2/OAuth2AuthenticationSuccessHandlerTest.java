package com.epam.spsa.security.oauth2;

import com.epam.spsa.service.JwtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:/messages.properties")
public class OAuth2AuthenticationSuccessHandlerTest {

    @InjectMocks
    private OAuth2AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler;

    @Mock
    private HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    @Mock
    private JwtService jwtService;

    @Spy
    private MockHttpServletRequest request;

    @Spy
    private MockHttpServletResponse response;

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    @Value("${jwt.authentication.cookie.expire-date}")
    private int jwtTokenCookieExpireDate;

    @Value("${jwt.token.authentication.expire-date}")
    private long jwtTokenAuthenticationExpireDate;

    @BeforeEach
    public void methodSetUp() {
        oAuth2AuthenticationSuccessHandler.setJwtTokenAuthenticationExpireDate(jwtTokenAuthenticationExpireDate);
        oAuth2AuthenticationSuccessHandler.setJwtTokenCookieName(jwtTokenCookieName);
        oAuth2AuthenticationSuccessHandler.setJwtTokenCookieExpireDate(jwtTokenCookieExpireDate);
        when(response.isCommitted()).thenReturn(false);
    }

    @Test
    public void shouldAddJwtTokenToCookie() throws IOException {
        String token = "token";
        when(jwtService.createToken(any(), any(), anyLong())).thenReturn(token);

        oAuth2AuthenticationSuccessHandler.onAuthenticationSuccess(request, response, OAuth2Builder.getUserAuthentication(true));

        verify(httpCookieOAuth2AuthorizationRequestRepository).removeAuthorizationRequestCookies(any(), any());
        verify(jwtService).createToken(any(), any(), anyLong());
        verify(response).addCookie(any());

        assertNotNull(response.getCookie(jwtTokenCookieName));
        assertEquals(response.getCookie(jwtTokenCookieName).getValue(), token);
    }

}
