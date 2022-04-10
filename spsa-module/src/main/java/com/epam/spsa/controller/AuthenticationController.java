package com.epam.spsa.controller;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.AuthenticationService;
import com.epam.spsa.utils.CookieUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile(value = "security")
@Api(tags = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @Value("${jwt.authentication.cookie.expire-date}")
    private int jwtCookieExpireDate;

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    @PostMapping(value = "/authenticate")
    @ApiOperation(value = "Sign in")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User authenticated", response = Map.class),
            @ApiResponse(code = 400, message = "Invalid credentials", response = ApiError.class)
    })
    public void authenticate(@Valid @RequestBody AuthDto authDto,
                             HttpServletResponse response) {
        String token = authenticationService.authenticate(authDto);
        CookieUtils.addCookie(response, jwtTokenCookieName, token, jwtCookieExpireDate);
    }

    @PostMapping(value = "/logout")
    @ApiOperation(value = "Logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        CookieUtils.deleteCookie(request, response, jwtTokenCookieName);
    }

}
