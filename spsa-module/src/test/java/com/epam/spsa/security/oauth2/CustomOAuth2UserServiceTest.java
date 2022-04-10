package com.epam.spsa.security.oauth2;

import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.OAuth2AuthenticationException;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.User;
import com.epam.spsa.service.SecurityUserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class CustomOAuth2UserServiceTest {

    @InjectMocks
    private CustomOAuth2UserService customOAuth2UserService;

    @Mock
    private SecurityUserService securityUserService;

    @Spy
    private ModelMapper mapper;

    @Test
    public void throwExceptionIfIncorrectProvider() {
        OAuth2UserRequest oAuth2UserRequest = new OAuth2UserRequest(
                CommonOAuth2Provider.GOOGLE
                        .getBuilder("Google")
                        .clientId("clientId")
                        .registrationId("Wrong")
                        .build(),
                getOAuth2AccessToken()
        );

        assertThrows(
                OAuth2AuthenticationException.class,
                () -> customOAuth2UserService.handleUser(oAuth2UserRequest, getOAuth2User(getAttributes())));
    }

    @Test
    public void throwExceptionIfUserProviderDoestMatch() {
        User user = getUser();
        user.setAuthProvider(AuthProvider.FACEBOOK);
        when(securityUserService.getByEmail(any())).thenReturn(user);

        Map<String, Object> attributes = getAttributes();
        attributes.put("email", "test@test.com");

        assertThrows(
                OAuth2AuthenticationException.class,
                () -> customOAuth2UserService.handleUser(getOAuth2UserRequest(), getOAuth2User(attributes)));
    }

    @Test
    public void shouldThrowExceptionIfEmailNull() {
        assertThrows(OAuth2AuthenticationException.class,
                () -> customOAuth2UserService.handleUser(getOAuth2UserRequest(), getOAuth2User(getAttributes())));

        verify(securityUserService, times(0)).getByEmail(any());
    }

    @Test
    public void tryRegisterUserIfNotExists() {
        when(securityUserService.getByEmail(any())).thenThrow(EntityNotFoundException.class);
        when(securityUserService.registerOAuth2(any(), any())).thenReturn(getUser());

        Map<String, Object> attributes = getAttributes();
        attributes.put("email", "test@test.com");
        customOAuth2UserService.handleUser(getOAuth2UserRequest(), getOAuth2User(attributes));

        verify(securityUserService).registerOAuth2(any(), any());
    }

    public static OAuth2UserRequest getOAuth2UserRequest() {
        return new OAuth2UserRequest(
                CommonOAuth2Provider.GOOGLE
                        .getBuilder("Google")
                        .clientId("clientId")
                        .build(),
                getOAuth2AccessToken()
        );
    }

    public static OAuth2AccessToken getOAuth2AccessToken() {
        return new OAuth2AccessToken(
                OAuth2AccessToken.TokenType.BEARER,
                "testToken",
                Instant.now(),
                Instant.now().plus(Duration.ofMinutes(1)));
    }

    public static OAuth2User getOAuth2User(Map<String, Object> attributes) {
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_TEST"));
        return new DefaultOAuth2User(authorities, attributes, "nothing");
    }

    public static Map<String, Object> getAttributes() {
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("nothing", "12156456");
        attributes.put("name", "Test Testovich");
        attributes.put("given_name", "Test");
        attributes.put("family_name", "Testovich");
        attributes.put("id", "1111");
        return attributes;
    }

    public static User getUser() {
        return User.builder()
                .name("Hello")
                .surname("World")
                .email("test@test.test")
                .authProvider(AuthProvider.GOOGLE)
                .build();
    }

}
