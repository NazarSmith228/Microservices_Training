package com.epam.slsa.converter;

import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.model.LocationType;
import com.epam.slsa.model.Placing;
import org.modelmapper.Converter;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

@Component
public class LocationTypeConverter implements Converter<LocationTypeDto, LocationType> {

    @Override
    public LocationType convert(MappingContext<LocationTypeDto, LocationType> context) {
        return LocationType
                .builder()
                .id(context.getSource().getId())
                .name(context.getSource().getName())
                .placing(Placing.getFromName(context.getSource().getPlacing()))
                .build();
    }

}
