package com.epam.spsa.dto.message;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Model that represents message`s sender. Accessible for GET requests")
public class MessageUserDto {

    private int id;
    private String name;
    private String surname;
    private String photo;

}
