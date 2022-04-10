package com.epam.spsa.security;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.security.auth.Subject;
import java.util.Collection;

@NoArgsConstructor
@AllArgsConstructor
public class JwtAuthenticationToken implements Authentication {

    private String token;

    private UserDetails userPrincipal;

    public JwtAuthenticationToken(String token) {
        this.token = token;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userPrincipal.getAuthorities();
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getDetails() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return userPrincipal;
    }

    @Override
    public boolean isAuthenticated() {
        return true;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    }

    @Override
    public String getName() {
        if (userPrincipal != null) {
            return userPrincipal.getUsername();
        } else {
            return null;
        }
    }

    @Override
    public boolean implies(Subject subject) {
        return false;
    }

    public String getToken() {
        return token;
    }

    public void setUserPrincipal(UserDetails userPrincipal) {
        this.userPrincipal = userPrincipal;
    }

}
