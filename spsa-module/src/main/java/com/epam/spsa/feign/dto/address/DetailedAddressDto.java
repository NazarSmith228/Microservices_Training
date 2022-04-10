package com.epam.spsa.feign.dto.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetailedAddressDto {

    private double latitude;
    private double longitude;
    private int locationId;

}
