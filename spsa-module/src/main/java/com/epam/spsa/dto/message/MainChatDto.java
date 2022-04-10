package com.epam.spsa.dto.message;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(value = "Model that represents chat with all messages and participants. Accessible for GET requests")
public class MainChatDto {

    private int id;
    private List<ChatMessageDto> chatMessages = new LinkedList<>();
    private Set<MessageUserDto> users = new LinkedHashSet<>();

}
