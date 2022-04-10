package com.epam.slsa.mapper;

import com.epam.slsa.converter.SportTypeListConverter;
import com.epam.slsa.converter.StringLinkTypeConverter;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.model.Coach;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CoachDtoMapper extends PropertyMap<CoachDto, Coach> {

    private final StringLinkTypeConverter stringLinkTypeConverter;

    @Override
    protected void configure() {
        skip(destination.getId());
        using(new SportTypeListConverter()).map(source.getSportTypes(), destination.getSportTypes());
        using(stringLinkTypeConverter).map(source.getLinks(), destination.getLinks());
    }

}
