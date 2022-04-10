package com.epam.spsa.dto.security;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResetPasswordDto {

    @NotNull(message = "{user.password.null}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,30}$", message = "{user.password.regex}")
    @ApiModelProperty(example = "012b44A3", notes = "Only latin letter, size minimum 8 maximum 30, "
            + "at least one uppercase letter, one lowercase letter and one number")
    private String oldPassword;

    @NotNull(message = "{user.password.null}")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{8,30}$", message = "{user.password.regex}")
    @ApiModelProperty(example = "012b44A3", notes = "Only latin letter, size minimum 8 maximum 30, "
            + "at least one uppercase letter, one lowercase letter and one number")
    private String newPassword;

}
