package com.epam.spsa.dto.user;

import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.message.ChatUserDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model that represents other users. Accessible for specific GET request")
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
    private boolean online;
    private String lastSeen;
    private String creationDate;
    private Set<RoleDto> roles;
    private Set<ChatUserDto> chats;

}
