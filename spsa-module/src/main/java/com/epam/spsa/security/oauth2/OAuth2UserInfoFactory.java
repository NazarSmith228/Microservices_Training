package com.epam.spsa.security.oauth2;

import com.epam.spsa.error.exception.OAuth2AuthenticationException;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.security.oauth2.user.FacebookOAuth2UserInfo;
import com.epam.spsa.security.oauth2.user.GoogleOAuth2UserInfo;
import com.epam.spsa.security.oauth2.user.OAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {

    public static OAuth2UserInfo getOAuth2UserInfo(String registrationId, Map<String, Object> attributes) {
        if (registrationId.equalsIgnoreCase(AuthProvider.GOOGLE.toString())) {
            return new GoogleOAuth2UserInfo(attributes);
        } else if (registrationId.equalsIgnoreCase(AuthProvider.FACEBOOK.toString())) {
            return new FacebookOAuth2UserInfo(attributes);
        } else {
            throw new OAuth2AuthenticationException("OAuth2 Provider is nor supported");
        }
    }

}
