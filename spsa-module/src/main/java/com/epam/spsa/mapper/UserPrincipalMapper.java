package com.epam.spsa.mapper;

import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
public class UserPrincipalMapper {

    public PropertyMap<User, UserPrincipal> toPrincipal = new PropertyMap<User, UserPrincipal>() {
        @Override
        protected void configure() {
            map(source, destination.getUser());
        }
    };

}
