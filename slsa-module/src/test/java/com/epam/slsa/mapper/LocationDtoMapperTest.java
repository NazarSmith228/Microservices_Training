package com.epam.slsa.mapper;

import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.model.Location;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationDto;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationForMappingTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class LocationDtoMapperTest {
    @Autowired
    private ModelMapper modelMapper;

    @Test
    public void locationMapperTest() {
        LocationDto locationDto = getLocationDto();
        Location location = modelMapper.map(locationDto, Location.class);
        Location newLocation = getLocationForMappingTest();

        assertEquals(location.toString(), newLocation.toString());
    }

}
