package com.epam.slsa.dto.coach;

import com.epam.slsa.dto.link.LinkDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.validation.SportTypeSubset;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CoachDto {

    @Min(value = 0)
    @Max(value = 5)
    @ApiModelProperty(example = "3", notes = "minimum 0 maximum 5")
    private double rating;

    @ApiModelProperty(example = "false", notes = "true or false")
    private boolean workWithChildren;

    @NotNull
    @Min(value = 1)
    @ApiModelProperty(notes = "user id that must exist in SPSA")
    private Integer userId;

    @SportTypeSubset
    @ApiModelProperty(notes = "Can be null")
    private Set<SportTypeDto> sportTypes;

    @Valid
    @NotNull
    private Set<LinkDto> links;

}
