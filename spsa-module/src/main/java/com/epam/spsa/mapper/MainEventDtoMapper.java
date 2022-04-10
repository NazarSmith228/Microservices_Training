package com.epam.spsa.mapper;

import com.epam.spsa.converter.UserIdListConverter;
import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.model.Event;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MainEventDtoMapper extends PropertyMap<Event, MainEventDto> {

    private final UserIdListConverter userIdListConverter;

    @Override
    protected void configure() {
        using(userIdListConverter).map(source.getAttendee(), destination.getUserIdList());
        map(source.getOwner().getId(), destination.getOwner_id());
    }

}
