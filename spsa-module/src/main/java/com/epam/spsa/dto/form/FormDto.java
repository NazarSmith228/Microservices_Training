package com.epam.spsa.dto.form;

import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.validation.RoleType;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FormDto {

    @Size(min = 5, max = 128)
    @ApiModelProperty(example = "Sport Life", notes = "size minimum 5 maximum 128")
    private String locationName;

    @Valid
    private AddressDto address;

    @NotNull
    @RoleType
    private RoleDto role;

}
