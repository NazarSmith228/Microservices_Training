package com.epam.spsa.service;

import com.epam.spsa.model.User;
import com.epam.spsa.service.impl.JwtServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@TestPropertySource(value = "classpath:/messages.properties")
public class JwtServiceTest {

    private static JwtServiceImpl jwtService;

    @BeforeAll
    public static void classSetUp() {
        jwtService = new JwtServiceImpl();
    }

    @Test
    public void validateCorrectTokenTest() {
        String token = jwtService.createToken(User.builder().email("test").build());
        Assertions.assertTrue(jwtService.validateToken(token));
    }

    @Test
    public void validateIncorrectTokenTest() {
        int index = 10;
        String token = jwtService.createToken(User.builder().email("test").build());
        StringBuilder tokenBuilder = new StringBuilder(token);
        tokenBuilder.setCharAt(index, (char)(tokenBuilder.charAt(index) + 1));
        Assertions.assertThrows(JwtException.class, () -> jwtService.validateToken(tokenBuilder.toString()));
    }

    @Test
    public void validateExpiredTokenTest() {
        String token = jwtService.createToken(User.builder().email("test").build(), 1);
        Assertions.assertThrows(ExpiredJwtException.class, () -> jwtService.validateToken(token));
    }

    @Test
    public void shouldThrowExceptionIfTokenIsEmpty() {
        Assertions.assertThrows(JwtException.class, () -> jwtService.getSubject(""));
    }

    @Value("${jwt.key}")
    public void setSecretKey(String secretKey) {
        jwtService.setSecretKey(secretKey);
    }

    @Value("${jwt.token.default-expire-date}")
    public void setDefaultExpireDate(long defaultExpireDate) {
        jwtService.setDefaultExpireDate(defaultExpireDate);
    }

}
