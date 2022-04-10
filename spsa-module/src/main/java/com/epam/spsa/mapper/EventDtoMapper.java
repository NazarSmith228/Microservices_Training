package com.epam.spsa.mapper;

import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.model.Event;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class EventDtoMapper {

    public PropertyMap<EventDto, Event> toModel = new PropertyMap<EventDto, Event>() {
        @Override
        protected void configure() {
            skip(destination.getAttendee());
            skip(destination.getId());
            skip(destination.getOwner());
        }
    };

}
