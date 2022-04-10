package com.epam.slsa.builders.sportType;

import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.SportType;

public class SportTypeBuilder {

    public static SportTypeDto getSportTypeDto() {
        return SportTypeDto.builder()
                .id(SportTypeInfo.id)
                .name(SportTypeInfo.name)
                .build();
    }

    public static SportType getSportType() {
        return SportType.builder()
                .id(SportTypeInfo.id)
                .name(SportTypeInfo.name)
                .coaches(null)
                .build();
    }

    public static SportType getSportTypeWithoutId() {
        return SportType.builder()
                .name(SportTypeInfo.name)
                .coaches(null)
                .build();
    }

}
