package com.epam.spsa.dto.sport;

import io.swagger.annotations.ApiModel;
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
@ApiModel(description = "Model that represents Sport Type. Accessible for GET requests")
public class SportTypeDto {

    @ApiModelProperty(example = "1")
    private int id;

    @NotBlank(message = "{sportType.name.blank}")
    @NotNull(message = "{sportType.name.null}")
    @ApiModelProperty(example = "Running", notes = "Only: running, swimming, football, yoga")
    private String name;

}
