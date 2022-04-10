package com.epam.spsa.security;

import com.epam.spsa.utils.CookieUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("security")
public class JwtAuthenticationLoginRetryFilter extends GenericFilterBean {

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie cookie = CookieUtils.getCookie((HttpServletRequest) request, jwtTokenCookieName);
        if (cookie != null) {
            log.info("Deleting {} token before login", jwtTokenCookieName);
            CookieUtils.deleteCookie(
                    (HttpServletRequest) request,
                    (HttpServletResponse) response,
                    jwtTokenCookieName);
        }
        chain.doFilter(request, response);
    }

}
