package com.epam.spsa.dto.user;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.validation.DurationValue;
import com.epam.spsa.validation.SportTypeValue;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class UserStatsDto {

    @SportTypeValue
    private SportTypeDto sportType;

    @ApiModelProperty(example = "155")
    @NotNull(message = "{user.stats.nullResult}")
    @Max(value = 200, message = "{user.stats.maxKm}")
    private double resultKm;

    @ApiModelProperty(dataType = "java.lang.String", example = "PT17H3M12S")
    @NotNull(message = "{user.stats.nullResult}")
    @DurationValue
    private String resultH;

    @ApiModelProperty(example = "1")
    private int locationId;

    @ApiModelProperty(example = "1")
    private int coachId;
}
