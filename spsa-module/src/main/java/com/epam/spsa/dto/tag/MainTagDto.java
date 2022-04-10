package com.epam.spsa.dto.tag;

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
public class MainTagDto {

    @ApiModelProperty(example = "2")
    private int id;

    @NotBlank(message = "{tag.name.blank}")
    @NotNull(message = "{tag.name.null}")
    @ApiModelProperty(example = "YOGA")
    private String name;

}
