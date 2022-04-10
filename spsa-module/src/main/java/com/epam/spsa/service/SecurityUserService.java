package com.epam.spsa.service;

import com.epam.spsa.dto.security.AuthDto;
import com.epam.spsa.dto.security.ResetPasswordDto;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.OAuth2UserInfo;

public interface SecurityUserService {

    String register(AuthDto authDto);

    User registerOAuth2(OAuth2UserInfo oAuth2UserInfo, String registrationId);

    User getByEmail(String email);

    User getByProviderId(String providerId);

    void updatePassword(ResetPasswordDto resetPasswordDto, String token);

    void activate(String email, String token);

}
