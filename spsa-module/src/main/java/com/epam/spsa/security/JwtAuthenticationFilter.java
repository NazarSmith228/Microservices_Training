package com.epam.spsa.security;

import com.epam.spsa.service.JwtService;
import com.epam.spsa.utils.CookieUtils;
import io.jsonwebtoken.JwtException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Data
@Component
@RequiredArgsConstructor
@Profile("security")
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final AuthenticationProvider authenticationProvider;

    private JwtService jwtService;

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String token = CookieUtils.getCookieValue((HttpServletRequest) request, jwtTokenCookieName);
        if (token == null || token.trim().isEmpty()) {
            log.info("{} cookie was not found", jwtTokenCookieName);
            chain.doFilter(request, response);
            return;
        }
        try {
            if (jwtService.validateToken(token)) {
                JwtAuthenticationToken authenticationToken = new JwtAuthenticationToken(token);
                Authentication authenticate = authenticationProvider.authenticate(authenticationToken);
                SecurityContextHolder.getContext().setAuthentication(authenticate);
            } else {
                CookieUtils.deleteCookie((HttpServletRequest) request, (HttpServletResponse) response, jwtTokenCookieName);
            }
        } catch (JwtException | BadCredentialsException ex) {
            CookieUtils.deleteCookie((HttpServletRequest) request, (HttpServletResponse) response, jwtTokenCookieName);
        }
        chain.doFilter(request, response);
    }

    @Autowired
    public void setJwtService(JwtService jwtService) {
        this.jwtService = jwtService;
    }

}
