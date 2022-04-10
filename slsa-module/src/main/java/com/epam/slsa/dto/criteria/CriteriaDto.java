package com.epam.slsa.dto.criteria;

import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.validation.PlacingType;
import com.epam.slsa.validation.SportTypeSubset;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CriteriaDto {

    @SportTypeSubset
    private Set<SportTypeDto> sportTypes;

    @Digits(integer = 9, fraction = 7)
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @ApiModelProperty(example = "150.0", notes = "minimum -180.0 maximum 180.0")
    private double latitude;

    @Digits(integer = 9, fraction = 7)
    @DecimalMin(value = "-180.0")
    @DecimalMax(value = "180.0")
    @ApiModelProperty(example = "-70.0", notes = "minimum -180.0 maximum 180.0")
    private double longitude;

    @PlacingType
    @ApiModelProperty(example = "Outdoor", notes = "Only: Indoor, Outdoor, Any")
    private String placing;

}
