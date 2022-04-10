package com.epam.spsa.security;

import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.SecurityUserService;
import com.epam.spsa.service.impl.JwtUserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class JwtUserDetailsServiceTest {

    @InjectMocks
    private JwtUserDetailsServiceImpl jwtUserDetailsService;

    @Mock
    private SecurityUserService securityUserService;

    @Spy
    private ModelMapper mapper;

    @Test
    public void userNotFoundByEmailTest() {
        when(securityUserService.getByEmail(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByUsername("email"));
        verify(securityUserService).getByEmail(any());
    }

    @Test
    public void successfulUserGetByEmailTest() {
        User user = getUser();
        when(securityUserService.getByEmail(any())).thenReturn(user);

        UserPrincipal userPrincipal = (UserPrincipal) jwtUserDetailsService.loadUserByUsername("email");

        assertEquals(user, userPrincipal.getUser());
    }

    @Test
    public void userNotFoundByProviderIdTest() {
        when(securityUserService.getByProviderId(any())).thenThrow(EntityNotFoundException.class);

        assertThrows(UsernameNotFoundException.class,
                () -> jwtUserDetailsService.loadUserByProviderId("providerId"));
        verify(securityUserService).getByProviderId(any());
    }

    @Test
    public void successfulUserGetByProviderIdTest() {
        User user = getUser();
        when(securityUserService.getByProviderId(any())).thenReturn(user);

        UserPrincipal userPrincipal = (UserPrincipal) jwtUserDetailsService.loadUserByProviderId("providerId");

        assertEquals(user, userPrincipal.getUser());
    }

    private static User getUser() {
        return User.builder()
                .email("email")
                .name("test")
                .surname("test")
                .password("password")
                .build();
    }

}
