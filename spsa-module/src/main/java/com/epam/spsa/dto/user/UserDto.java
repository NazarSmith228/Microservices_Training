package com.epam.spsa.dto.user;

import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.validation.GenderType;
import com.epam.spsa.validation.LocalDateType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel(description = "Model that represents other users. Accessible for POST and PUT requests")
public class UserDto {

    @NotNull(message = "{user.name.null}")
    @Pattern(regexp = "^[a-zA-z]{2,20}$", message = "{user.name.regex}")
    @ApiModelProperty(example = "Victor", notes = "Only latin letter, size minimum 2 maximum 20")
    private String name;

    @NotNull(message = "{user.surname.null}")
    @Pattern(regexp = "^[a-zA-z]{2,20}$", message = "{user.surname.regex}")
    @ApiModelProperty(example = "Pikyluk", notes = "Only latin letter, size minimum 2 maximum 20")
    private String surname;

    @NotNull(message = "{user.password.null}")
    @Pattern(regexp = "^(?=.*[a-z\\.\\$\\/])(?=.*[A-Z\\.\\$\\/])(?=.*\\d)[a-zA-Z\\.\\$\\/\\d]{8,128}$", message = "{user.password.regex}")
    @ApiModelProperty(example = "012b44A3", notes = "Only latin letter, size minimum 8 maximum 128, "
            + "at least one uppercase letter, one lowercase letter and one number")
    private String password;

    @NotNull(message = "{user.email.null}")
    @Size(max = 30, message = "{user.email.size}")
    @Pattern(regexp = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$", message = "{user.email.regex}")
    @ApiModelProperty(example = "210372@ukr.net", notes = "Only latin letter, size minimum 9 maximum 30")
    private String email;

    @Pattern(regexp = "^((\\+38)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "{user.phoneNumber.regex}")
    @ApiModelProperty(example = "0980258933", notes = "size minimum 7,maximum 10")
    private String phoneNumber;

    @LocalDateType
    @ApiModelProperty(example = "2000-05-30", notes = "minimum 12, maximum 100")
    private String dateOfBirth;

    @NotNull(message = "{user.address.null}")
    @Valid
    private AddressDto address;

    @NotNull(message = "{user.gender.null}")
    @NotBlank(message = "{user.gender.blank}")
    @GenderType
    @ApiModelProperty(example = "male", notes = "Only latin letter, Only: male, both, female")
    private String gender;

    @ApiModelProperty(example = "true", notes = "true or false")
    private boolean hasChildren;


}