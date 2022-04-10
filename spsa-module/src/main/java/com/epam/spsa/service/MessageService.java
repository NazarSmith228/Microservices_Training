package com.epam.spsa.service;

import com.epam.spsa.dto.message.MainMessageDto;
import com.epam.spsa.dto.message.MessageDto;
import com.epam.spsa.dto.message.WebSocketMessageDto;

import java.util.List;

public interface MessageService {

    MainMessageDto save(MessageDto messageDto, int chatId, int userId);

    MainMessageDto getById(int messageId);

    void delete(int messageId, int userId);

    MainMessageDto update(int messageId, MessageDto messageDto, int userId);

    List<MainMessageDto> getAllByChatId(int chatId, int userId);

    WebSocketMessageDto updateMessage(MessageDto messageDto, int messageId, int chatId, int userId);

    WebSocketMessageDto deleteMessage(int messageId, int chatId, int userId);

}
