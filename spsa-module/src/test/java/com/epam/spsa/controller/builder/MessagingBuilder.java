package com.epam.spsa.controller.builder;

import com.epam.spsa.dto.message.MainMessageDto;
import com.epam.spsa.dto.message.MessageDto;
import com.epam.spsa.dto.message.MessageUserDto;
import com.epam.spsa.model.Chat;
import com.epam.spsa.model.Message;
import com.epam.spsa.model.User;

import java.time.LocalDateTime;

public class MessagingBuilder {

    public static Chat getChat() {
        return Chat
                .builder()
                .build();
    }

    public static Message getMessage() {
        return Message
                .builder()
                .chat(getChat())
                .message("Message")
                .sendingDate(LocalDateTime.now())
                .sender(getUser())
                .build();

    }

    public static User getUser() {
        return User
                .builder()
                .build();
    }

    public static MainMessageDto getMainMessageDto() {
        return MainMessageDto
                .builder()
                .sendingDate("2020-09-07")
                .message("A message")
                .build();
    }

    public static MessageDto getMessageDto() {
        return MessageDto
                .builder()
                .message("A message")
                .build();
    }

}
