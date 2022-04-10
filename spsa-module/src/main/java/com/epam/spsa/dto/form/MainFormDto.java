package com.epam.spsa.dto.form;

import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.security.RoleDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainFormDto {

    private int id;
    private String locationName;
    private AddressDto address;
    private RoleDto role;

}
