package com.epam.slsa.dto.link;

import com.epam.slsa.validation.LinkType;
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
public class LinkDto {

    @Size(max = 2048, message = "{link.url.size}")
    @ApiModelProperty(example = "https://www.youtube.com/watch?v=xINCA3RvqUE",
            notes = "Only latin letter, size maximum 2048")
    private String url;

    @NotNull(message = "{link.type.null}")
    @NotBlank(message = "{link.type.blank}")
    @LinkType
    @ApiModelProperty(example = "youtube", notes = "Only youtube, instagram, facebook")
    private String type;

}
