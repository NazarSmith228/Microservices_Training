package com.epam.spsa.security.oauth2.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public abstract class OAuth2UserInfo {

    protected Map<String, Object> attributes;

    public abstract String getProviderId();

    public abstract String getFullName();

    public abstract String getName();

    public abstract String getSurname();

    public abstract String getEmail();

}
