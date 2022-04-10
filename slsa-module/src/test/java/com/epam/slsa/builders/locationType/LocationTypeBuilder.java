package com.epam.slsa.builders.locationType;

import com.epam.slsa.model.LocationType;
import com.epam.slsa.model.Placing;

public class LocationTypeBuilder {

    public static LocationType getLocationType() {
        return LocationType.builder()
                .id(LocationTypeInfo.id)
                .name(LocationTypeInfo.name)
                .locations(null)
                .placing(Placing.OUTDOOR)
                .build();
    }

    public static LocationType getLocationTypeWithoutId() {
        return LocationType.builder()
                .name(LocationTypeInfo.name)
                .placing(Placing.OUTDOOR)
                .build();
    }

}
