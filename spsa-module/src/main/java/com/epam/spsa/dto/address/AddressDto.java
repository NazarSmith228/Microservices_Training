package com.epam.spsa.dto.address;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Model that represents Address. Accessible for POST and GET request")
public class AddressDto {

    @Digits(integer = 9, fraction = 7, message = "{address.latitude.digits}")
    @DecimalMin(value = "-180.0", message = "{address.latitude.min}")
    @DecimalMax(value = "180.0", message = "{address.latitude.max}")
    @ApiModelProperty(example = "100.214", notes = "Less than or equal 9 characters before comma and 7 characters after")
    private double latitude;

    @Digits(integer = 9, fraction = 7, message = "{address.longitude.digits}")
    @DecimalMin(value = "-180.0", message = "{address.longitude.min}")
    @DecimalMax(value = "180.0", message = "{address.longitude.max}")
    @ApiModelProperty(example = "2.3021", notes = "Less than or equal 9 characters before comma and 7 characters after")
    private double longitude;

}
