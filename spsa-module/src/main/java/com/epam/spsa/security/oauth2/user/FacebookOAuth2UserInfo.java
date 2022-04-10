package com.epam.spsa.security.oauth2.user;

import java.util.Map;

public class FacebookOAuth2UserInfo extends OAuth2UserInfo {

    public FacebookOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderId() {
        return String.valueOf(attributes.get("id"));
    }

    @Override
    public String getFullName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getName() {
        return (String) attributes.get("first_name");
    }

    @Override
    public String getSurname() {
        return (String) attributes.get("last_name");
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String toString() {
        return "FacebookOAuth2UserInfo{" +
                "attributes=" + attributes +
                '}';
    }

}
