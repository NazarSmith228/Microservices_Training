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
@ApiModel(value = "Model that represents message with all the information about it. Accessible for GET requests")
public class MainMessageDto {

    private int id;
    private MessageUserDto sender;
    private String message;
    private String sendingDate;
    private int chatId;

}
