package com.epam.slsa.feign.dto;

import com.epam.slsa.dto.address.AddressDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MainUserDto {

    private int id;
    private String name;
    private String surname;
    private String email;
    private String password;
    private String phoneNumber;
    private String photo;
    private String dateOfBirth;
    private AddressDto address;
    private String gender;
    private boolean hasChildren;
    private String creationDate;
    private Set<RoleDto> roles;

}