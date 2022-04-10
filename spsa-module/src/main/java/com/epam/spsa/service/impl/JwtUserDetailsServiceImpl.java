package com.epam.spsa.service.impl;

import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Profile("security")
public class JwtUserDetailsServiceImpl implements UserDetailsService {

    private final SecurityUserService securityUserService;

    private final ModelMapper mapper;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user;
        try {
            user = securityUserService.getByEmail(email);
        } catch (EntityNotFoundException ex) {
            throw new UsernameNotFoundException("User with email: " + email + " doesn't exists");
        }
        return mapper.map(user, UserPrincipal.class);
    }

    public UserDetails loadUserByProviderId(String providerId) throws UsernameNotFoundException {
        User user;
        try {
            user = securityUserService.getByProviderId(providerId);
        } catch (EntityNotFoundException ex) {
            throw new UsernameNotFoundException("User with providerId: " + providerId + " doesn't exists");
        }
        return mapper.map(user, UserPrincipal.class);
    }

}
