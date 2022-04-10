package com.epam.slsa.builders.location;

import com.epam.slsa.builders.coach.CoachDtoBuilder;
import com.epam.slsa.builders.locationType.LocationTypeBuilder;
import com.epam.slsa.builders.sportType.SportTypeBuilder;
import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coach.MainCoachDto;
import com.epam.slsa.dto.location.CriteriaLocationDto;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.*;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.slsa.builders.address.AddressBuilder.getAddress;
import static com.epam.slsa.builders.location.LocationInfo.*;

public class LocationDtoBuilder {

    public static LocationDto getLocationDto() {
        AddressDto address = AddressDto.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
        LocationTypeDto locationTypeDto = LocationTypeDto.builder()
                .id(locationTypeId)
                .name(locationType)
                .placing("Indoor")
                .build();
        List<SportTypeDto> sportTypeDtos = new ArrayList<>();
        sportTypeDtos.add(new SportTypeDto(2, "Football"));

        return LocationDto.builder()
                .name(locationName)
                .address(address)
                .locationType(locationTypeDto)
                .sportTypes(Sets.newHashSet(sportTypeDtos))
                .build();
    }

    public static Location getLocationForMappingTest() {
        Address address1 = Address.builder()
                .longitude(longitude)
                .latitude(latitude)
                .build();
        LocationType locationType1 = LocationType.builder()
                .id(locationTypeId)
                .name(locationType)
                .placing(Placing.INDOOR)
                .build();
        List<SportType> sportTypes = new ArrayList<>();
        sportTypes.add(new SportType(2, "Football", null, null));

        return Location.builder()
                .name(locationName)
                .address(address1)
                .locationType(locationType1)
                .sportTypes(Sets.newHashSet(sportTypes))
                .build();

    }

    public static Location getLocationWithoutId() {
        Address address = getAddress();
        LocationType locationTypes = LocationTypeBuilder.getLocationType();
        List<SportType> sportTypes = new ArrayList<>();
        sportTypes.add(SportTypeBuilder.getSportType());

        return Location.builder()
                .name(locationName)
                .address(address)
                .locationType(locationTypes)
                .sportTypes(Sets.newHashSet(sportTypes))
                .build();
    }

    public static CriteriaLocationDto getCriteriaLocationDto() {
        AddressDto address = AddressDto.builder()
                .longitude(latitude)
                .latitude(longitude)
                .build();

        LocationTypeDto locationTypeDto = LocationTypeDto.builder()
                .id(locationTypeId)
                .name(locationType)
                .placing("Indoor")
                .build();

        List<CriteriaCoachDto> coaches = Lists.newArrayList(CoachDtoBuilder.getCriteriaCoachDto());

        Set<SportTypeDto> sportTypeDtos = new HashSet<>();
        sportTypeDtos.add(new SportTypeDto(2, "Football"));

        return CriteriaLocationDto.builder()
                .id(locationId)
                .name(locationName)
                .address(address)
                .locationType(locationTypeDto)
                .locationSchedule(null)
                .coaches(coaches)
                .sportTypes(sportTypeDtos)
                .build();
    }

    public static MainLocationDto getMainLocationDto() {
        AddressDto address = AddressDto.builder()
                .longitude(latitude)
                .latitude(longitude)
                .build();

        LocationTypeDto locationTypeDto = LocationTypeDto.builder()
                .id(locationTypeId)
                .name(locationType)
                .placing("Indoor")
                .build();

        List<MainCoachDto> coaches = Lists.newArrayList(CoachDtoBuilder.getMainCoachDto());

        Set<SportTypeDto> sportTypeDtos = new HashSet<>();
        sportTypeDtos.add(new SportTypeDto(2, "Football"));

        return MainLocationDto.builder()
                .id(locationId)
                .name(locationName)
                .address(address)
                .locationType(locationTypeDto)
                .locationSchedule(null)
                .coaches(coaches)
                .sportTypes(sportTypeDtos)
                .build();
    }

    public static Location getLocation() {
        Address address = getAddress();
        LocationType location = LocationTypeBuilder.getLocationType();
        List<SportType> sportTypes = new ArrayList<>();
        sportTypes.add(SportTypeBuilder.getSportType());

        return Location.builder()
                .id(2)
                .name(locationName)
                .address(address)
                .locationType(location)
                .sportTypes(Sets.newHashSet(sportTypes))
                .build();
    }

}
