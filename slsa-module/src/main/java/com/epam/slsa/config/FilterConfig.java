package com.epam.slsa.config;

import com.epam.slsa.filter.AccessControlHeaderFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<AccessControlHeaderFilter> loggingFilter() {
        FilterRegistrationBean<AccessControlHeaderFilter> registrationBean
                = new FilterRegistrationBean<>();

        registrationBean.setFilter(new AccessControlHeaderFilter());
        registrationBean.addUrlPatterns("/api/v1/*");

        return registrationBean;
    }

}
