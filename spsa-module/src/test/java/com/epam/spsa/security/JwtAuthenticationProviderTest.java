package com.epam.spsa.security;

import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.service.impl.JwtUserDetailsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class JwtAuthenticationProviderTest {

    @InjectMocks
    private JwtAuthenticationProvider authenticationProvider;

    @Mock
    private JwtUserDetailsServiceImpl userDetailsService;

    @Mock
    private JwtService jwtService;

    @Test
    public void shouldThrowBadCredentialsIfUserNotFoundByEmail() {
        String email = "email";

        when(jwtService.getSubject(any())).thenReturn(email);
        when(jwtService.getClaim(any(), eq("authProvider"))).thenReturn("LOCAL");
        when(jwtService.getClaim(any(), eq("providerId"))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(email)).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(BadCredentialsException.class,
                () -> authenticationProvider.authenticate(new JwtAuthenticationToken("token")));

        verify(userDetailsService, times(0)).loadUserByProviderId(any());
        verify(userDetailsService).loadUserByUsername(email);
    }

    @Test
    public void shouldThrowBadCredentialsIfUserNotFoundByProviderId() {
        String providerId = "1111";
        when(jwtService.getSubject(any())).thenReturn(null);
        when(jwtService.getClaim(any(), eq("authProvider"))).thenReturn("GOOGLE");
        when(jwtService.getClaim(any(), eq("providerId"))).thenReturn("1111");
        when(userDetailsService.loadUserByProviderId(providerId)).thenThrow(UsernameNotFoundException.class);

        Assertions.assertThrows(BadCredentialsException.class,
                () -> authenticationProvider.authenticate(new JwtAuthenticationToken("token")));

        verify(userDetailsService, times(0)).loadUserByUsername(any());
        verify(userDetailsService).loadUserByProviderId(providerId);
    }

    @Test
    public void authenticateByEmailTest() {
        String email = "email";

        UserDetails userDetails = getUserDetails();

        when(jwtService.getSubject(any())).thenReturn(email);
        when(jwtService.getClaim(any(), eq("authProvider"))).thenReturn("LOCAL");
        when(jwtService.getClaim(any(), eq("providerId"))).thenReturn(null);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(userDetails);

        JwtAuthenticationToken authenticationToken =
                (JwtAuthenticationToken) authenticationProvider.authenticate(new JwtAuthenticationToken("token"));

        assertNotNull(authenticationToken.getPrincipal());

        UserDetails authenticationDetails = (UserDetails) authenticationToken.getPrincipal();

        assertEquals(userDetails.getUsername(), authenticationDetails.getUsername());
        assertEquals(userDetails.getPassword(), authenticationDetails.getPassword());
    }

    @Test
    public void authenticateByProviderId() {
        String providerId = "1111";

        UserDetails userDetails = getUserDetails();

        when(jwtService.getSubject(any())).thenReturn(null);
        when(jwtService.getClaim(any(), eq("authProvider"))).thenReturn("GOOGLE");
        when(jwtService.getClaim(any(), eq("providerId"))).thenReturn(providerId);
        when(userDetailsService.loadUserByProviderId(providerId)).thenReturn(userDetails);

        JwtAuthenticationToken authenticationToken =
                (JwtAuthenticationToken) authenticationProvider.authenticate(new JwtAuthenticationToken("token"));

        assertNotNull(authenticationToken.getPrincipal());

        UserDetails authenticationDetails = (UserDetails) authenticationToken.getPrincipal();

        assertEquals(userDetails.getUsername(), authenticationDetails.getUsername());
        assertEquals(userDetails.getPassword(), authenticationDetails.getPassword());
    }

    private static UserDetails getUserDetails() {
        return UserPrincipal.builder()
                .user(User.builder()
                        .email("email")
                        .name("test")
                        .surname("test")
                        .password("password")
                        .build()
                ).build();
    }

}
