package com.epam.spsa.service;

import com.epam.spsa.dto.message.MainChatDto;

import java.util.List;

public interface ChatService {

    MainChatDto save(int firstId, int secondId);

    MainChatDto getById(int chatId);

    void delete(int chatId);

    List<MainChatDto> getAllByUserId(int id);

}
