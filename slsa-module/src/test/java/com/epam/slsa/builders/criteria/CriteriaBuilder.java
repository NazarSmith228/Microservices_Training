package com.epam.slsa.builders.criteria;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.sportType.SportTypeDto;

import java.util.HashSet;
import java.util.Set;

import static com.epam.slsa.builders.criteria.CriteriaInfo.*;
import static com.epam.slsa.builders.sportType.SportTypeBuilder.getSportTypeDto;

public class CriteriaBuilder {

    public static CriteriaDto getCriteriaDto() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(getSportTypeDto());
        return CriteriaDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .placing(placing)
                .sportTypes(sportTypes)
                .build();
    }

    public static CriteriaDto getCriteria() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(getSportTypeDto());
        return CriteriaDto.builder()
                .latitude(latitude)
                .longitude(longitude)
                .placing(placing)
                .sportTypes(sportTypes)
                .build();
    }

}
