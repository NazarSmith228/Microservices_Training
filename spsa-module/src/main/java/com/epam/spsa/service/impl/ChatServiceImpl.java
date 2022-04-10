package com.epam.spsa.service.impl;

import com.epam.spsa.dao.ChatDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.message.MainChatDto;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.SameParticipantException;
import com.epam.spsa.model.Chat;
import com.epam.spsa.model.User;
import com.epam.spsa.service.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource(value = "classpath:/exceptionMessages.properties")
@SuppressWarnings("Duplicates")
public class ChatServiceImpl implements ChatService {

    private final ChatDao chatDao;

    private final UserDao userDao;

    private final ModelMapper mapper;

    @Value("${user.exception.notfound}")
    private String userNotFoundByIdMessage;

    @Value("${chat.exception.notfound}")
    private String chatNotFoundByIdMessage;

    @Value("${user.same.participant.exception}")
    private String sameParticipantMessage;

    @Value("${chat.already.exists}")
    private String chatAlreadyExistsMessage;

    @Override
    public MainChatDto save(int firstId, int secondId) {
        if (firstId == secondId) {
            throw new SameParticipantException(sameParticipantMessage);
        }
        User firstParticipant = getUserById(firstId);
        User secondParticipant = getUserById(secondId);
        Set<User> users = new LinkedHashSet<>(Arrays.asList(firstParticipant, secondParticipant));
        List<Chat> existedChats = chatDao.getAll();
        boolean exists = existedChats
                .stream()
                .map(Chat::getUsers)
                .anyMatch(x -> x.containsAll(users));
        if (exists) {
            throw new EntityAlreadyExistsException(chatAlreadyExistsMessage);
        }
        Chat chat = Chat
                .builder()
                .users(users)
                .build();
        return mapper.map(chatDao.save(chat), MainChatDto.class);
    }

    @Override
    public MainChatDto getById(int chatId) {
        return mapper.map(getChatById(chatId), MainChatDto.class);
    }

    @Override
    public void delete(int chatId) {
        Chat chat = getChatById(chatId);
        chatDao.delete(chat);
    }

    @Override
    public List<MainChatDto> getAllByUserId(int id) {
        return getUserById(id).getUserChats()
                .stream()
                .map(chat -> mapper.map(chat, MainChatDto.class))
                .collect(Collectors.toList());
    }

    private Chat getChatById(int id) {
        log.info("Getting Chat by id: {}", id);
        Chat chat = chatDao.getById(id);
        if (chat == null) {
            log.error("Chat wasn't found. id: {}", id);
            throw new EntityNotFoundException(chatNotFoundByIdMessage + id);
        }
        log.info("Found Chat: {}", chat);
        return chat;
    }

    private User getUserById(int id) {
        log.info("Getting User by id: {}", id);
        User user = userDao.getById(id);
        if (user == null) {
            log.error("User wasn't found. id: {}", id);
            throw new EntityNotFoundException(userNotFoundByIdMessage + id);
        }
        log.info("Found User: {}", user);
        return user;
    }

}
