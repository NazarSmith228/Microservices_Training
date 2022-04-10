package com.epam.spsa.security.oauth2;

import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.JwtService;
import com.epam.spsa.utils.CookieUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@Slf4j
@Component
@RequiredArgsConstructor
@Profile("security")
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final HttpCookieOAuth2AuthorizationRequestRepository httpCookieOAuth2AuthorizationRequestRepository;

    private final JwtService jwtService;

    @Value("${oauth2.redirect-uri.cookie.name}")
    private String redirectUriCookieName;

    @Value("${jwt.token.cookie.name}")
    private String jwtTokenCookieName;

    @Value("${jwt.authentication.cookie.expire-date}")
    private int jwtTokenCookieExpireDate;

    @Value("${jwt.token.authentication.expire-date}")
    private long jwtTokenAuthenticationExpireDate;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String targetUrl = determineTargetUrl(request, response, authentication);

        if (response.isCommitted()) {
            log.info("Response has already been committed. Unable to redirect to: " + targetUrl);
            return;
        }

        Map<String, Object> claims = new HashMap<>();
        claims.put("authProvider", userPrincipal.getUser().getAuthProvider().getName());
        claims.put("providerId", userPrincipal.getUser().getProviderId());

        String token = jwtService.createToken(userPrincipal.getUser(), claims, jwtTokenAuthenticationExpireDate);
        CookieUtils.addCookie(response, jwtTokenCookieName, token, jwtTokenCookieExpireDate);

        clearAuthenticationAttributes(request, response);

        getRedirectStrategy().sendRedirect(request, response, targetUrl);
    }

    @Override
    protected String determineTargetUrl(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String redirectUri = CookieUtils.getCookieValue(request, redirectUriCookieName);
        if (redirectUri == null) {
            redirectUri = getDefaultTargetUrl();
        }
        return redirectUri;
    }

    protected void clearAuthenticationAttributes(HttpServletRequest request, HttpServletResponse response) {
        super.clearAuthenticationAttributes(request);
        httpCookieOAuth2AuthorizationRequestRepository.removeAuthorizationRequestCookies(request, response);
    }

}
