package com.epam.spsa.service.impl;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.AuthenticationService;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@Profile("security")
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final SecurityUserService securityUserService;
    private final JwtService jwtService;

    private final ModelMapper mapper;

    private final PasswordEncoder passwordEncoder;

    @Value("${user.credentials.incorrect}")
    private String userCredentialsIncorrect;

    @Value("${jwt.token.authentication.expire-date}")
    private long jwtTokenAuthenticationExpireDate;

    /**
     * Authenticates user by username and password
     *
     * @param authDto contains username and password
     * @return JWT Authentication Token
     */
    @Override
    public String authenticate(AuthDto authDto) {
        User user;

        try {
            user = securityUserService.getByEmail(authDto.getEmail());
        } catch (EntityNotFoundException ex) {
            throw new BadCredentialsException(userCredentialsIncorrect);
        }

        if (!verifyUser(authDto, user)) {
            throw new BadCredentialsException(userCredentialsIncorrect);
        }

        log.info("User successfully authenticated");
        UserPrincipal principal = mapper.map(user, UserPrincipal.class);

        Map<String, Object> claims = new HashMap<>();
        claims.put("authProvider", AuthProvider.LOCAL.getName());
        claims.put("providerId", null);
        return jwtService.createToken(principal.getUser(), claims, jwtTokenAuthenticationExpireDate);
    }

    private boolean verifyUser(AuthDto authDto, User user) {
        return user.getAuthProvider().equals(AuthProvider.LOCAL)
                && user.isEnabled()
                && user.getEmail().equals(authDto.getEmail())
                && passwordEncoder.matches(authDto.getPassword(), user.getPassword());
    }

}
