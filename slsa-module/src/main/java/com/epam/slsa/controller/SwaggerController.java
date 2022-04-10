package com.epam.slsa.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
class SwaggerController {

    private static final String REDIRECT_SWAGGER_UI_HTML = "redirect:/swagger-ui.html";

    @GetMapping(value = {"", "/swagger"})
    public String redirectSwagger() {
        log.debug("Redirecting from / to /swagger-ui.html");
        return REDIRECT_SWAGGER_UI_HTML;
    }

}
