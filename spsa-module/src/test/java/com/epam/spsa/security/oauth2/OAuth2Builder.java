package com.epam.spsa.security.oauth2;

import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationExchange;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

public class OAuth2Builder {

    private static String clientId = "web-client";
    private static Set<GrantedAuthority> authorities = new HashSet<>();
    private static String registrationId = "google";
    private static String authorizationUri = "https://localhost:8080";
    private static String redirectUri = "https://localhost:8080";

    static {
        authorities.add(new SimpleGrantedAuthority("user"));
    }

    public static OAuth2LoginAuthenticationToken getUserAuthentication(boolean enabled) {
        UserPrincipal userPrincipal = new UserPrincipal();
        String email = "test";
        String password = "test";
        userPrincipal.setUser(
                User.builder()
                        .email(email)
                        .password(password)
                        .enabled(enabled)
                        .authProvider(AuthProvider.LOCAL)
                        .providerId("1111")
                        .build());

        return new OAuth2LoginAuthenticationToken(
                getClientRegistration(),
                getOAuth2AuthenticationExchange(),
                userPrincipal,
                authorities,
                new OAuth2AccessToken(
                        OAuth2AccessToken.TokenType.BEARER,
                        "token",
                        Instant.now(),
                        Instant.now().plus(Duration.ofMinutes(1))),
                null);
    }

    public static OAuth2AuthorizationExchange getOAuth2AuthenticationExchange() {
        return new OAuth2AuthorizationExchange(
                getOAuth2AuthorizationRequest(),
                OAuth2AuthorizationResponse
                        .success("OK")
                        .redirectUri(redirectUri)
                        .build());
    }

    public static OAuth2AuthorizationRequest getOAuth2AuthorizationRequest() {
        return OAuth2AuthorizationRequest.implicit()
                .clientId(clientId)
                .authorizationUri(authorizationUri)
                .redirectUri(redirectUri)
                .build();
    }

    public static ClientRegistration getClientRegistration() {
        return CommonOAuth2Provider.GOOGLE
                .getBuilder(registrationId)
                .clientId(clientId)
                .authorizationUri(authorizationUri)
                .build();
    }

}
