package com.epam.spsa.config;

import com.epam.spsa.security.AccessControlHeaderFilter;
import com.epam.spsa.security.JwtAuthenticationLoginRetryFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
@Profile("security")
public class FilterConfig {

    private final JwtAuthenticationLoginRetryFilter jwtAuthenticationLoginRetryFilter;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationLoginRetryFilter> deleteLoginJwtAuthenticationTokenFilter() {
        FilterRegistrationBean<JwtAuthenticationLoginRetryFilter> registrationBean =
                new FilterRegistrationBean<>();
        registrationBean.setOrder(-101);
        registrationBean.setFilter(jwtAuthenticationLoginRetryFilter);
        registrationBean.addUrlPatterns(
                "/register",
                "/activate-user",
                "/reset-password/request",
                "/reset-password/update",
                "/authenticate",
                "/oauth2/*",
                "/callback/*");
        return registrationBean;
    }

    @Bean
    public FilterRegistrationBean<AccessControlHeaderFilter> corsFilter() {
        FilterRegistrationBean<AccessControlHeaderFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setOrder(-102);
        registrationBean.setFilter(new AccessControlHeaderFilter());
        registrationBean.addUrlPatterns("*");

        return registrationBean;
    }

}

