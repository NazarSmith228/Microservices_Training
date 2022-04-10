package com.epam.slsa.dto.locationSchedule;

import com.epam.slsa.validation.DayType;
import com.epam.slsa.validation.LocalTimeType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationScheduleDto {

    @NotNull(message = "{day.null}")
    @NotBlank(message = "{day.blank}")
    @DayType
    @ApiModelProperty(example = "monday", notes = "Only: monday, tuesday, wednesday, thursday, friday, saturday, sunday")
    private String day;

    @NotNull(message = "{startTime.null}")
    @NotBlank(message = "{startTime.blank}")
    @LocalTimeType
    @ApiModelProperty(example = "09:00", notes = "(Two number 00 to 23):(Two number 00 to 59)")
    private String startTime;


    @NotNull(message = "{endTime.null}")
    @NotBlank(message = "{endTime.blank}")
    @ApiModelProperty(example = "18:00", notes = "(Two number 00 to 23):(Two number 00 to 59)")
    @LocalTimeType
    private String endTime;

}
