package com.epam.spsa.security.oauth2;

import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.OAuth2AuthenticationException;
import com.epam.spsa.model.AuthProvider;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.OAuth2UserInfo;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.SecurityUserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
@Profile("security")
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final SecurityUserService securityUserService;

    private final ModelMapper mapper;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth2Request:\nAccess token: {}\nScopes: {}\nAdditional parameters: {}\n",
                userRequest.getAccessToken().getTokenType().getValue(),
                userRequest.getAccessToken().getScopes(),
                userRequest.getAdditionalParameters());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        log.info("OAuth2User: {}", oAuth2User);

        try {
            return handleUser(userRequest, oAuth2User);
        } catch (Exception e) {
            throw new InternalAuthenticationServiceException(e.getMessage());
        }
    }

    public OAuth2User handleUser(OAuth2UserRequest userRequest, OAuth2User oAuth2User) {
        String registrationId = userRequest.getClientRegistration().getRegistrationId();

        OAuth2UserInfo oAuth2UserInfo = OAuth2UserInfoFactory
                .getOAuth2UserInfo(registrationId, oAuth2User.getAttributes());

        log.info("{}", oAuth2UserInfo);

        User user;
        try {
            user = getUserByOauth2Info(oAuth2UserInfo);
            if ( !user.getAuthProvider().equals(AuthProvider.fromName(registrationId))) {
                throw new OAuth2AuthenticationException("Incorrect OAuth provider");
            }
        } catch (EntityNotFoundException ex) {
            user = securityUserService.registerOAuth2(oAuth2UserInfo, registrationId);
        }

        UserPrincipal userPrincipal = mapper.map(user, UserPrincipal.class);
        userPrincipal.setAttributes(oAuth2User.getAttributes());

        log.info("{}", userPrincipal);
        return userPrincipal;
    }

    private User getUserByOauth2Info(OAuth2UserInfo oAuth2UserInfo) {
        User user;
        if (oAuth2UserInfo.getEmail() != null) {
            user = securityUserService.getByEmail(oAuth2UserInfo.getEmail());
        } else {
            throw new OAuth2AuthenticationException("Email must be set in your account");
            //user = securityUserService.getByProviderId(oAuth2UserInfo.getProviderId());
        }
        return user;
    }

}
