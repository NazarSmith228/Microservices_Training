package com.epam.spsa.security.oauth2;

import com.epam.spsa.utils.CookieUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.security.oauth2.client.web.AuthorizationRequestRepository;
import org.springframework.security.oauth2.core.endpoint.OAuth2AuthorizationRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * An implementation of an {@link AuthorizationRequestRepository} that stores
 * {@link OAuth2AuthorizationRequest} in the Cookie.
 */
@Slf4j
@Data
@Component
@Profile("security")
public class HttpCookieOAuth2AuthorizationRequestRepository implements AuthorizationRequestRepository<OAuth2AuthorizationRequest> {

    private static final int cookieExpireSeconds = 180;

    @Value("${oauth2.auth.request.cookie.name}")
    private String oauth2AuthorizationRequestCookieName;

    @Value("${oauth2.redirect-uri.cookie.name}")
    private String redirectUriCookieName;

    @Override
    public OAuth2AuthorizationRequest loadAuthorizationRequest(HttpServletRequest request) {
        log.info("Loading OAuth2AuthorizationRequest from Request");
        Cookie cookie = CookieUtils.getCookie(request, oauth2AuthorizationRequestCookieName);
        if (cookie == null) {
            return null;
        }
        return CookieUtils.deserialize(cookie, OAuth2AuthorizationRequest.class);
    }

    @Override
    public void saveAuthorizationRequest(OAuth2AuthorizationRequest authorizationRequest, HttpServletRequest request, HttpServletResponse response) {
        if (authorizationRequest == null) {
            removeAuthorizationRequestCookies(request, response);
            return;
        }

        log.info("Saving OAuth2AuthorizationRequest into cookie\n" +
                        "OAuth2AuthorizationRequest:\n " +
                        "Attributes: {}\n" +
                        " Additional parameters: {}\n " +
                        "Scopes: {}\n " +
                        "Authorization request uri: {}\n " +
                        "Authorization uri: {}\n " +
                        "Client id: {}\n " +
                        "Grant type: {}\n " +
                        "Redirect uri: {}\n " +
                        "Response type: {}\n " +
                        "State: {}",
                authorizationRequest.getAttributes(),
                authorizationRequest.getAdditionalParameters(),
                authorizationRequest.getScopes(),
                authorizationRequest.getAuthorizationRequestUri(),
                authorizationRequest.getAuthorizationUri(),
                authorizationRequest.getClientId(),
                authorizationRequest.getGrantType().getValue(),
                authorizationRequest.getRedirectUri(),
                authorizationRequest.getResponseType().getValue(),
                authorizationRequest.getState());

        CookieUtils.addCookie(response, oauth2AuthorizationRequestCookieName, CookieUtils.serialize(authorizationRequest), cookieExpireSeconds);

        String uriValue = request.getParameter(redirectUriCookieName);
        if (uriValue != null && ! uriValue.trim().isEmpty()) {
            CookieUtils.addCookie(response, redirectUriCookieName, uriValue, cookieExpireSeconds);
        }
    }

    @Override
    public OAuth2AuthorizationRequest removeAuthorizationRequest(HttpServletRequest request) {
        return this.loadAuthorizationRequest(request);
    }

    public void removeAuthorizationRequestCookies(HttpServletRequest request, HttpServletResponse response) {
        log.info("Deleting authorization cookies");
        CookieUtils.deleteCookie(request, response, oauth2AuthorizationRequestCookieName);
        CookieUtils.deleteCookie(request, response, redirectUriCookieName);
    }

}
