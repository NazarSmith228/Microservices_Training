package com.epam.spsa.controller;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.dto.security.ResetPasswordDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.model.User;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.service.SecurityUserService;
import io.jsonwebtoken.JwtException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@Profile(value = "security")
@Api(tags = "Registration")
public class RegistrationController {

    private final SecurityUserService securityUserService;

    private final JwtService jwtService;

    private final MailClient mailClient;

    @Value("${jwt.token.reset-password}")
    private long resetTokenExpireDate;

    @Value("${register.activation.redirect-url}")
    private String registerActivationRedirectUrl;

    @Value("${resetPassword.redirect-url}")
    private String restPasswordRedirectUrl;

    @Value("${spsa.connect.url}")
    private String host;

    @Value("${jwt.token.incorrect}")
    private String invalidTokenMassage;

    @PostMapping(value = "/register")
    @ApiOperation(value = "Register user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New user created"),
            @ApiResponse(code = 400, message = "Validation error", response = ApiError.class)
    })
    public void register(@Valid @RequestBody AuthDto authDto) {
        securityUserService.register(authDto);
    }

    @GetMapping(value = "/activate-user")
    @ApiOperation(value = "Activate user")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User successfully activated"),
            @ApiResponse(code = 400, message = "Incorrect token", response = ApiError.class)
    })
    public void activateUser(@RequestParam("token") String token, HttpServletResponse response) throws IOException {
        String email = jwtService.getSubject(token);
        securityUserService.activate(email, token);
        response.sendRedirect(registerActivationRedirectUrl);
    }

    @PostMapping(value = "/reset-password/request")
    @ApiOperation(value = "Send reset password request")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Request successfully sent"),
            @ApiResponse(code = 400, message = "Incorrect token", response = ApiError.class),
            @ApiResponse(code = 404, message = "User not found by email", response = ApiError.class)
    })
    public void requestPasswordReset(
            @NotNull(message = "{user.email.null}")
            @Size(max = 30, message = "{user.email.size}")
            @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{user.email.regex}")
            @RequestBody String email) {
        User user = securityUserService.getByEmail(email);
        String token = jwtService.createToken(user, resetTokenExpireDate);
        mailClient.sendEmail("Reset password",
                user.getEmail(),
                host + "reset-password/activate?token=" + token);
    }

    @GetMapping(value = "/reset-password/activate")
    @ApiOperation(value = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(code = 303, message = "Redirect to reset password page"),
            @ApiResponse(code = 400, message = "Incorrect token", response = ApiError.class),
    })
    public void activateResetToken(@RequestParam String token, HttpServletResponse response) throws IOException {
        if (jwtService.validateToken(token)) {
            response.sendRedirect(restPasswordRedirectUrl + "?token=" + token);
        } else {
            throw new JwtException(invalidTokenMassage);
        }
    }

    @PostMapping(value = "/reset-password/update")
    @ApiOperation(value = "Reset password")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Password successfully reset"),
            @ApiResponse(code = 400, message = "Incorrect password", response = ApiError.class)
    })
    public void resetPassword(@Valid @RequestBody ResetPasswordDto resetPasswordDto,
                              @RequestParam String token) {
        securityUserService.updatePassword(resetPasswordDto, token);
    }

}
