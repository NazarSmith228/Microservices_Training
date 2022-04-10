package com.epam.slsa.dto.coachCriteria;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoachCriteriaDto {

    @Min(value = 0)
    @Max(value = 5)
    @ApiModelProperty(example = "5", notes = "minimum 0 maximum 5; Can be null")
    private double rating;

    @ApiModelProperty(example = "Football", notes = "Only latin letter, Only: Running, Swimming, Football, Yoga")
    private String sportType;

    @ApiModelProperty(example = "female", notes = "Only latin letter, Only: male, both, female")
    private String gender;

    @ApiModelProperty(example = "false", notes = "true or false")
    private Boolean workWithChildren;

    @ApiModelProperty(example = "YogaMaster", notes = "Can be null")
    private String locationName;

    @ApiModelProperty(example = "6")
    private int userId;

}
