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
@ApiModel(value = "Message model that is represented in chat. Accessible for GET requests")
public class ChatMessageDto {

    private int id;
    private MessageUserDto sender;
    private String message;
    private String sendingDate;

}
