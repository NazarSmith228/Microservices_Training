package com.epam.spsa.service.impl;

import com.epam.spsa.dao.ChatDao;
import com.epam.spsa.dao.MessageDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.message.MainMessageDto;
import com.epam.spsa.dto.message.MessageDto;
import com.epam.spsa.dto.message.WebSocketMessageDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.UserNotParticipantException;
import com.epam.spsa.error.exception.UserNotSenderException;
import com.epam.spsa.model.Chat;
import com.epam.spsa.model.Message;
import com.epam.spsa.model.User;
import com.epam.spsa.service.MessageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
@PropertySource(value = "classpath:/exceptionMessages.properties")
@SuppressWarnings("Duplicates")
public class MessageServiceImpl implements MessageService {

    private final MessageDao messageDao;

    private final UserDao userDao;

    private final ChatDao chatDao;

    private final ModelMapper modelMapper;

    @Value("${user.exception.notfound}")
    private String userNotFoundByIdMessage;

    @Value("${chat.exception.notfound}")
    private String chatNotFoundByIdMessage;

    @Value("${message.exception.notfound}")
    private String messageNotFoundByIdMessage;

    @Value("${user.not.participant.exception}")
    private String userNotParticipantMessage;

    @Override
    public MainMessageDto save(MessageDto messageDto, int chatId, int userId) {
        User user = getUserById(userId);
        Chat chat = getChatById(chatId);
        isParticipant(chat.getUsers(), userId);
        Message message = Message
                .builder()
                .sender(user)
                .message(messageDto.getMessage())
                .sendingDate(LocalDateTime.now())
                .chat(chat)
                .build();
        return modelMapper.map(messageDao.save(message), MainMessageDto.class);
    }

    @Override
    public MainMessageDto getById(int messageId) {
        return modelMapper.map(getMessageById(messageId), MainMessageDto.class);
    }

    @Override
    public void delete(int messageId, int userId) {
        Message message = getMessageById(messageId);
        Chat chat = message.getChat();
        isParticipant(chat.getUsers(), userId);
        messageDao.delete(message);
    }

    @Override
    public MainMessageDto update(int messageId, MessageDto messageDto, int userId) {
        Message oldMessage = getMessageById(messageId);
        User user = oldMessage.getSender();
        if (user.getId() != userId) {
            throw new UserNotSenderException("Current user is not the sender of this message");
        }
        oldMessage.setMessage(messageDto.getMessage());
        return modelMapper.map(messageDao.update(oldMessage), MainMessageDto.class);
    }

    @Override
    public List<MainMessageDto> getAllByChatId(int chatId, int userId) {
        Chat chat = getChatById(chatId);
        isParticipant(chat.getUsers(), userId);
        return chat.getChatMessages()
                .stream()
                .map(message -> modelMapper.map(message, MainMessageDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public WebSocketMessageDto updateMessage(MessageDto messageDto, int messageId, int chatId, int userId) {
        Message oldMessage = getMessageById(messageId);
        User user = oldMessage.getSender();
        if (user.getId() != userId) {
            throw new UserNotSenderException("Current user is not the sender of this message");
        }
        oldMessage.setMessage(messageDto.getMessage());
        MainMessageDto updatedMessage = modelMapper.map(messageDao.update(oldMessage), MainMessageDto.class);

        return WebSocketMessageDto
                .builder()
                .delete(false)
                .update(true)
                .mainMessageDto(updatedMessage)
                .build();
    }

    @Override
    public WebSocketMessageDto deleteMessage(int messageId, int chatId, int userId) {
        Message message = getMessageById(messageId);
        Chat chat = message.getChat();
        isParticipant(chat.getUsers(), userId);
        MainMessageDto deletedMessage = modelMapper.map(message, MainMessageDto.class);
        messageDao.delete(message);

        return WebSocketMessageDto
                .builder()
                .delete(true)
                .update(false)
                .mainMessageDto(deletedMessage)
                .build();
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

    private Message getMessageById(int id) {
        log.info("Getting Message by id: {}", id);
        Message message = messageDao.getById(id);
        if (message == null) {
            log.error("Chat wasn't found. id: {}", id);
            throw new EntityNotFoundException(messageNotFoundByIdMessage + id);
        }
        log.info("Found Message: {}", message);
        return message;
    }

    private void isParticipant(Set<User> users, int userId) {
        boolean participant = users
                .stream()
                .noneMatch(x -> x.getId() == userId);
        if (participant) {
            throw new UserNotParticipantException(userNotParticipantMessage);
        }
    }
}
