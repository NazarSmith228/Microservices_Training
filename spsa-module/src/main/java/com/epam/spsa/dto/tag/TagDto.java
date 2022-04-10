package com.epam.spsa.dto.tag;

import com.epam.spsa.validation.TagValue;
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
public class TagDto {

    @NotBlank(message = "{tag.name.blank}")
    @NotNull(message = "{tag.name.null}")
    @ApiModelProperty(example = "YOGA")
    @TagValue
    private String name;

}
