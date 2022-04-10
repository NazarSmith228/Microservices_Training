package com.epam.slsa.mapper;

import com.epam.slsa.converter.LocationTypeConverter;
import com.epam.slsa.converter.SportTypeListConverter;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.model.Location;
import lombok.RequiredArgsConstructor;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LocationDtoMapper extends PropertyMap<LocationDto, Location> {

    private final SportTypeListConverter sportTypeListConverter;
    private final LocationTypeConverter locationTypeConverter;

    @Override
    protected void configure() {
        using(sportTypeListConverter).map(source.getSportTypes(), destination.getSportTypes());
        using(locationTypeConverter).map(source.getLocationType(), destination.getLocationType());
    }

}