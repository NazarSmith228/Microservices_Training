package com.epam.spsa.dto.user;

import com.epam.spsa.model.Gender;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model that represents partial users. Accessible for specific GET request for comment")
public class PartialUserDto {
    private int id;
    private String name;
    private String surname;
    private String email;
    private String photo;
}
