package com.epam.spsa.service;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.User;
import com.epam.spsa.service.impl.AuthenticationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.Mockito.*;

@ExtendWith({SpringExtension.class})
public class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationServiceImpl authenticationService;

    @Spy
    private ModelMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @Mock
    private SecurityUserService securityUserService;

    @Test
    public void throwExceptionIfUserDoesntExists() {
        when(securityUserService.getByEmail(any()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.authenticate(getUserAuthDto()));
    }

    @Test
    public void throwExceptionIfCredentialsDoesntMatch() {
        when(securityUserService.getByEmail(any())).thenReturn(getUser());
        when(passwordEncoder.matches(any(), any())).thenReturn(true);

        Assertions.assertThrows(
                BadCredentialsException.class,
                () -> authenticationService.authenticate(getUserAuthDto()));
    }

    @Test
    public void successfulAuthenticationTest() {
        User user = getUser();
        user.setEnabled(true);

        when(securityUserService.getByEmail(any())).thenReturn(user);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.createToken(any(), any(), anyLong())).thenReturn("validToken");

        String token = authenticationService.authenticate(getUserAuthDto());
        Assertions.assertEquals("validToken", token);

        verify(securityUserService).getByEmail(any());
        verify(mapper).map(any(), any());
    }

    public static User getUser() {
        return User.builder()
                .email("email")
                .password("password")
                .enabled(false)
                .authProvider(AuthProvider.LOCAL)
                .build();
    }

    public static AuthDto getUserAuthDto() {
        return AuthDto.builder()
                .email("email")
                .password("password")
                .build();
    }

}
