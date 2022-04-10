package com.epam.spsa.dto.event;

import com.epam.spsa.validation.Date;
import com.epam.spsa.validation.Time;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "Model that represents calendar event. Accessible for POST and PUT requests")
public class EventDto {

    @Date
    @ApiModelProperty(example = "2020-04-14", notes = "format yyyy-mm-dd")
    private String date;

    @Time
    @ApiModelProperty(example = "10:10:10", notes = "format hh:mm:ss")
    private String time;

    @ApiModelProperty(example = "Sport event!!!", notes = "maximum 2048")
    private String description;

    @ApiModelProperty(example = "5")
    private int location_id;

    @NotNull
    @ApiModelProperty(example = "1")
    private int owner_id;

    @ApiModelProperty(example = "[1,2]")
    private Set<Integer> userIdList;

}
