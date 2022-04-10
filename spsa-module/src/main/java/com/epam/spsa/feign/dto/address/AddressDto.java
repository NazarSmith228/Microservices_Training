package com.epam.spsa.feign.dto.address;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AddressDto {

    @Digits(integer = 9, fraction = 6)
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @ApiModelProperty(example = "150.0", notes = "minimum -180.0 maximum 180.0")
    private double latitude;

    @Digits(integer = 9, fraction = 6)
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @ApiModelProperty(example = "-70.0", notes = "minimum -180.0 maximum 180.0")
    private double longitude;

}
