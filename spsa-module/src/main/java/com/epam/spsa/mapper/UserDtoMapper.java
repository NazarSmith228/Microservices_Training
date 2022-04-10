package com.epam.spsa.mapper;

import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.model.User;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class UserDtoMapper extends PropertyMap<UserDto, User> {

    @Override
    protected void configure() {
        map(null, destination.getAddress().getUser());
    }

}
