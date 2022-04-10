package com.epam.slsa.dto.comment;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    @NotNull
    @ApiModelProperty(example = "1")
    private int userId;

    @NotNull
    @NotBlank
    @ApiModelProperty(example = "Raising funds", notes = "Minimum 5 characters, maximum 150, not blank")
    private String comment;

    @Min(value = 1)
    @Max(value = 5)
    @ApiModelProperty(example = "3", notes = "minimum 1 maximum 5")
    private int rating;

}
