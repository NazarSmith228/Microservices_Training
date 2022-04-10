package com.epam.spsa.service.impl;

import com.epam.spsa.dao.RoleDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.dto.security.ResetPasswordDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.IncorrectPasswordException;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.Role;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.OAuth2UserInfo;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.service.SecurityUserService;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@Profile("security")
public class SecurityUserServiceImpl implements SecurityUserService {

    private final UserDao userDao;
    private final RoleDao roleDao;

    private final JwtService jwtService;

    private final ModelMapper mapper;

    private final MailClient mailClient;

    private final PasswordEncoder passwordEncoder;

    @Value("${user.email.notfound}")
    private String userNotFoundByEmailMessage;

    @Value("${user.providerId.notfound}")
    private String userNotFoundByProviderIdMessage;

    @Value("${jwt.token.incorrect}")
    private String invalidTokenMassage;

    @Value("${user.password.reset.notMatch}")
    private String userPasswordNotMatchMessage;

    @Value("${user.register.exists}")
    private String userAlreadyExistsMessage;

    @Value("${spsa.connect.url}")
    private String host;

    @Override
    public String register(AuthDto authDto) {
        try {
            User user = getByEmail(authDto.getEmail());
            if (user != null) {
                throw new BadCredentialsException(userAlreadyExistsMessage);
            }
        } catch (EntityNotFoundException ignored) {
        }

        User newUser = mapper.map(authDto, User.class);

        newUser.setCreationDate(LocalDateTime.now());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));

        newUser.setAuthProvider(AuthProvider.LOCAL);

        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getByName("USER"));
        newUser.setRoles(roles);

        userDao.save(newUser);

        String token = jwtService.createToken(newUser);

        mailClient.sendEmail("Activate account",
                authDto.getEmail(),
                host + "activate-user?token=" + token);
        return token;
    }

    @Override
    public User registerOAuth2(OAuth2UserInfo oAuth2UserInfo, String registrationId) {
        log.info("Register new user via OAuth2 user info");

        User newUser = new User();

        mapToUser(oAuth2UserInfo, newUser);

        newUser.setEnabled(true);
        newUser.setCreationDate(LocalDateTime.now());
        newUser.setAuthProvider(AuthProvider.valueOf(registrationId.toUpperCase()));

        Set<Role> roles = new HashSet<>();
        roles.add(roleDao.getByName("USER"));
        newUser.setRoles(roles);

        log.info("New user: {}", newUser);

        userDao.save(newUser);
        return newUser;
    }

    @Override
    public User getByEmail(String email) {
        try {
            return userDao.getByEmail(email);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(userNotFoundByEmailMessage + email);
        }
    }

    @Override
    public User getByProviderId(String providerId) {
        try {
            return userDao.getByProviderId(providerId);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(userNotFoundByProviderIdMessage + providerId);
        }
    }

    @Override
    public void activate(String email, String token) {
        if (jwtService.validateToken(token)) {
            User user = userDao.getByEmail(email);
            user.setEnabled(true);
            userDao.save(user);
        } else {
            throw new JwtException(invalidTokenMassage);
        }
    }

    @Override
    public void updatePassword(ResetPasswordDto resetPasswordDto, String token) {
        try {
            if (jwtService.validateToken(token)) {
                String email = jwtService.getSubject(token);
                User user = userDao.getByEmail(email);
                if (passwordEncoder.matches(resetPasswordDto.getOldPassword(), user.getPassword())) {
                    user.setPassword(passwordEncoder.encode(resetPasswordDto.getNewPassword()));
                    userDao.update(user);
                } else {
                    throw new IncorrectPasswordException(userPasswordNotMatchMessage);
                }
            } else {
                throw new JwtException(invalidTokenMassage);
            }
        } catch (JwtException ex) {
            throw new JwtException(invalidTokenMassage);
        }
    }

    private void mapToUser(OAuth2UserInfo oAuth2UserInfo, User newUser) {
        newUser.setEmail(oAuth2UserInfo.getEmail());
        newUser.setName(oAuth2UserInfo.getName());
        newUser.setSurname(oAuth2UserInfo.getSurname());
        newUser.setProviderId(oAuth2UserInfo.getProviderId());
    }

}
