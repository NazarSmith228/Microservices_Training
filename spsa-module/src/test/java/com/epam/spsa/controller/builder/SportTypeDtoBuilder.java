package com.epam.spsa.controller.builder;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.model.SportType;

public class SportTypeDtoBuilder {

    public static SportTypeDto getSportTypeDto() {
        return SportTypeDto.builder()
                .name("Running")
                .build();
    }

    public static SportType getSportType() {
        return SportType.builder()
                .id(1)
                .name("Running")
                .build();
    }

}
