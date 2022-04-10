package com.epam.slsa.dto.sportType;

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
public class SportTypeDto {

    @ApiModelProperty(example = "1")
    private int id;

    @NotNull(message = "{sportType.name.null}")
    @NotBlank(message = "{sportType.name.blank}")
    @ApiModelProperty(example = "Running", notes = "Only: running, swimming, football, yoga")
    private String name;

}
