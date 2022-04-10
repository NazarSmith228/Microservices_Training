package com.epam.spsa.service;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.security.ResetPasswordDto;
import com.epam.spsa.error.exception.IncorrectPasswordException;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.model.User;
import com.epam.spsa.service.impl.SecurityUserServiceImpl;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith({SpringExtension.class})
public class SecurityUserServiceTest {

    @InjectMocks
    private SecurityUserServiceImpl securityUserService;

    @Mock
    private JwtService jwtService;

    @Mock
    private MailClient mailClient;

    @Mock
    private UserDao userDao;

    @Mock
    private RoleDao roleDao;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Test
    public void shouldThrowExceptionIfActivationTokenIsNotValid() {
        when(jwtService.validateToken(any())).thenReturn(false);

        Assertions.assertThrows(JwtException.class,
                () -> securityUserService.activate("", ""));
    }

    @Test
    public void activateUserSuccessfullyTest() {
        User user = getUser();

        when(jwtService.validateToken(any())).thenReturn(true);
        when(userDao.getByEmail(any())).thenReturn(user);

        securityUserService.activate("", "");

        assertTrue(user.isEnabled());
    }

    @Test
    public void shouldThrowExceptionIfResetTokenIsNotValid() {
        when(jwtService.validateToken(any())).thenReturn(false);

        Assertions.assertThrows(JwtException.class,
                () -> securityUserService.updatePassword(null, null));
    }

    @Test
    public void shouldThrowExceptionIfCurrentPasswordDontMatch() {
        when(passwordEncoder.matches(any(), any())).thenReturn(false);
        when(jwtService.validateToken(any())).thenReturn(true);
        when(jwtService.getSubject(any())).thenReturn("email");
        when(userDao.getByEmail(any())).thenReturn(getUser());

        Assertions.assertThrows(IncorrectPasswordException.class,
                () -> securityUserService.updatePassword(getResetPasswordDto(), "token"));
    }

    @Test
    public void successfulPasswordResetTest() {
        String encodedPassword = "encoded";
        User user = getUser();

        when(passwordEncoder.encode(any())).thenReturn(encodedPassword);
        when(passwordEncoder.matches(any(), any())).thenReturn(true);
        when(jwtService.validateToken(any())).thenReturn(true);
        when(jwtService.getSubject(any())).thenReturn("email");
        when(userDao.getByEmail(any())).thenReturn(user);

        securityUserService.updatePassword(getResetPasswordDto(), "token");

        assertEquals(encodedPassword, user.getPassword());

        verify(passwordEncoder).encode(any());
        verify(userDao).update(any());
    }

    private static User getUser() {
        return User.builder()
                .name("Maksym")
                .surname("Natural")
                .email("210372@ukr.net")
                .enabled(false)
                .build();
    }

    private static ResetPasswordDto getResetPasswordDto() {
        return ResetPasswordDto.builder()
                .newPassword("new")
                .oldPassword("old")
                .build();
    }

}

