package com.epam.slsa.dto.location;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.validation.LocationTypeSubset;
import com.epam.slsa.validation.SportTypeSubset;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDto {

    @NotNull(message = "{location.name.null}")
    @NotBlank(message = "{location.name.blank}")
    @Size(min = 3, max = 32)
    @ApiModelProperty(example = "Konoha", notes = "Only latin letter, size minimum 3 maximum 32")
    private String name;

    @NotNull(message = "{location.address.null}")
    @Valid
    private AddressDto address;

    @NotNull(message = "{locationType.null}")
    @LocationTypeSubset
    private LocationTypeDto locationType;

    @Pattern(regexp = "^((\\+38)[\\- ]?)?(\\(?\\d{3}\\)?[\\- ]?)?[\\d\\- ]{7,10}$",
            message = "{coach.phoneNumber.pattern}")
    @ApiModelProperty(example = "0980265122", notes = "Size minimum 7, maximum 10")
    private String phoneNumber;

    @Size(max = 64)
    @Pattern(regexp = "(https?:\\/\\/)?([\\w\\-])+\\.{1}([a-zA-Z]{2,63})([\\/\\w-]*)*\\/?\\??([^#\\n\\r]*)?#?([^\\n\\r]*)",
            message = "{url.regexp}")
    @ApiModelProperty(example = "https://location-web-site.com")
    private String webSite;

    @SportTypeSubset
    private Set<SportTypeDto> sportTypes;

}
