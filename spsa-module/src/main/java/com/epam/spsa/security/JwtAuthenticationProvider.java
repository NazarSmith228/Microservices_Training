package com.epam.spsa.security;

import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.service.impl.JwtUserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("security")
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final JwtUserDetailsServiceImpl userDetailsService;

    private final JwtService jwtService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken jwtAuthenticationToken = (JwtAuthenticationToken) authentication;
        String email = jwtService.getSubject(jwtAuthenticationToken.getToken());
        String authProvider = jwtService.getClaim(jwtAuthenticationToken.getToken(), "authProvider");
        String providerId = jwtService.getClaim(jwtAuthenticationToken.getToken(), "providerId");

        UserDetails userDetails;
        try {
            if (AuthProvider.LOCAL.equals(AuthProvider.fromName(authProvider))) {
                userDetails = userDetailsService.loadUserByUsername(email);
            } else {
                userDetails = userDetailsService.loadUserByProviderId(providerId);
            }
        } catch (UsernameNotFoundException | UnsupportedOperationException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        jwtAuthenticationToken.setUserPrincipal(userDetails);
        return jwtAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }

}
