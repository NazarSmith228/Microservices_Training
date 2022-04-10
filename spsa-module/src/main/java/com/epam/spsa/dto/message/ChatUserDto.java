package com.epam.spsa.dto.message;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Chat model that is represented in user. Accessible for GET requests")
public class ChatUserDto {

    private int id;
    private Set<MessageUserDto> users = new LinkedHashSet<>();

}
