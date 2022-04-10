package com.epam.spsa.feign.dto.location;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.feign.dto.address.AddressDto;
import com.epam.spsa.feign.dto.locationType.LocationTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {

    private String name;
    private AddressDto address;
    private LocationTypeDto locationType;
    private Set<SportTypeDto> sportTypes;

}
