package com.epam.slsa.dto.locationType;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationTypeDto {

    @ApiModelProperty(example = "1")
    private int id;

    @NotNull(message = "{locationType.name.null}")
    @NotBlank(message = "{locationType.name.blank}")
    @Size(min = 3, max = 32)
    @ApiModelProperty(example = "Park", notes = "size minimum 3 maximum 32")
    private String name;

    @ApiModelProperty(example = "Outdoor", notes = "Only: indoor, outdoor, any")
    private String placing;

}
