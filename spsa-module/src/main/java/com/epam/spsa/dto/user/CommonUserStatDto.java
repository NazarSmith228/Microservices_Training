package com.epam.spsa.dto.user;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.validation.commonUserStatDto.CoachesIdSet;
import com.epam.spsa.validation.commonUserStatDto.DateValue;
import com.epam.spsa.validation.commonUserStatDto.LocationIdSet;
import com.epam.spsa.validation.commonUserStatDto.SportTypesSet;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "Model that represents request for static of user")
public class CommonUserStatDto {

    @ApiModelProperty(example = "2018-04-23", notes = "Start interval of time")
    @DateValue
    private LocalDate startOfInterval;

    @ApiModelProperty(example = "2020-04-21", notes = "End interval of time")
    @DateValue
    private LocalDate endOfInterval;

    @ApiModelProperty(notes = "List of sport types")
    @SportTypesSet
    private Set<SportTypeDto> sportTypes;

    @ApiModelProperty(example = "[1]", notes = "List of locations id")
    @LocationIdSet
    private Set<Integer> locations;

    @ApiModelProperty(example = "[1]", notes = "List of coaches id")
    @CoachesIdSet
    private Set<Integer> coaches;
}
