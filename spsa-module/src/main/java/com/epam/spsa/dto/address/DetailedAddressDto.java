package com.epam.spsa.dto.address;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model that represents Address. Accessible for specific GET request")
public class DetailedAddressDto {

    private double latitude;
    private double longitude;
    private int userId;

}
