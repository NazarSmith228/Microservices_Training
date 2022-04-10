package com.epam.spsa.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RoleDto {

    @ApiModelProperty(example = "1")
    private int id;

    @ApiModelProperty(example = "USER", notes = "Only: USER, COACH, SUPER_ADMIN, VENUE_ADMIN")
    private String name;

}
