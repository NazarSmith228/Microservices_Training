package com.epam.spsa.service;

import com.epam.spsa.dao.ChatDao;
import com.epam.spsa.dao.MessageDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.message.MainMessageDto;
import com.epam.spsa.dto.message.MessageDto;
import com.epam.spsa.dto.message.MessageUserDto;
import com.epam.spsa.dto.message.WebSocketMessageDto;
import com.epam.spsa.error.exception.UserNotParticipantException;
import com.epam.spsa.error.exception.UserNotSenderException;
import com.epam.spsa.model.Chat;
import com.epam.spsa.model.Message;
import com.epam.spsa.model.User;
import com.epam.spsa.service.impl.MessageServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class MessageServiceTest {

    @InjectMocks
    private MessageServiceImpl messageService;

    @Mock
    private MessageDao messageDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ChatDao chatDao;

    @Mock
    private ModelMapper modelMapper;

    @Test
    public void saveTest() {
        int chatId = 1;
        int userId = 2;
        MessageDto messageDto = getMessageDtos().get(0);

        when(userDao.getById(userId)).thenReturn(getUsers().get(0));
        when(chatDao.getById(chatId)).thenReturn(getChats().get(1));
        when(messageDao.save(any())).thenReturn(getMessages().get(0));
        when(modelMapper.map(any(), eq(MainMessageDto.class))).thenReturn(getMainMessageDtos().get(0));

        MainMessageDto mainMessageDto = messageService.save(messageDto, chatId, userId);

        assertEquals(getMainMessageDtos().get(0), mainMessageDto);
    }

    @Test
    public void throwUserNotParticipantExceptionTest() {
        int chatId = 1;
        int userId = 2;
        MessageDto messageDto = getMessageDtos().get(0);

        when(userDao.getById(userId)).thenReturn(getUsers().get(0));
        when(chatDao.getById(chatId)).thenReturn(getChats().get(0));

        assertThrows(UserNotParticipantException.class, () -> messageService.save(messageDto, chatId, userId));
    }

    @Test
    public void getMessageByIdTest() {
        int messageId = 1;

        when(messageDao.getById(messageId)).thenReturn(getMessages().get(0));
        when(modelMapper.map(Mockito.any(Message.class), eq(MainMessageDto.class))).thenReturn(getMainMessageDtos().get(0));

        MainMessageDto mainMessageDto = messageService.getById(messageId);

        assertEquals(getMainMessageDtos().get(0), mainMessageDto);
    }

    @Test
    public void deleteMessageTest() {
        int messageId = 2;
        int userId = 1;

        when(messageDao.getById(messageId)).thenReturn(getMessages().get(1));
        doNothing().when(messageDao).delete(getMessages().get(1));

        assertDoesNotThrow(() -> messageService.delete(messageId, userId));
    }

    @Test
    public void deleteMessageForWebSocketTest() {
        int messageId = 2;
        int chatId = 1;
        int userId = 1;
        WebSocketMessageDto webSocketMessageDto = WebSocketMessageDto
                .builder()
                .delete(true)
                .update(false)
                .mainMessageDto(getMainMessageDtos().get(1))
                .build();

        when(messageDao.getById(messageId)).thenReturn(getMessages().get(1));
        when(modelMapper.map(any(), eq(MainMessageDto.class))).thenReturn(getMainMessageDtos().get(1));
        doNothing().when(messageDao).delete(getMessages().get(1));

        WebSocketMessageDto webSocketMessageDtoResult = messageService.deleteMessage(messageId, chatId, userId);

        assertEquals(webSocketMessageDto, webSocketMessageDtoResult);
    }

    @Test
    public void throwUserNotParticipantExceptionMessageTest() {
        int messageId = 2;
        int userId = 2;

        when(messageDao.getById(messageId)).thenReturn(getMessages().get(1));

        assertThrows(UserNotParticipantException.class, () -> messageService.delete(messageId, userId));
    }

    @Test
    public void updateMessageTest() {
        int messageId = 2;
        int userId = 2;
        MessageDto messageDto = getMessageDtos().get(0);
        Message message = getMessages().get(1);

        when(messageDao.getById(messageId)).thenReturn(message);
        message.setMessage(messageDto.getMessage());
        when(messageDao.update(message)).thenReturn(message);
        when(modelMapper.map(message, MainMessageDto.class)).thenReturn(getMainMessageDtos().get(1));

        MainMessageDto mainMessageDto = messageService.update(messageId, messageDto, userId);

        assertEquals(getMainMessageDtos().get(1), mainMessageDto);
    }

    @Test
    public void updateMessageForWebSocketTest() {
        int messageId = 2;
        int userId = 2;
        int chatId = 1;
        MessageDto messageDto = getMessageDtos().get(0);
        Message message = getMessages().get(1);
        WebSocketMessageDto webSocketMessageDto = WebSocketMessageDto
                .builder()
                .delete(false)
                .update(true)
                .mainMessageDto(getMainMessageDtos().get(1))
                .build();

        when(messageDao.getById(messageId)).thenReturn(message);
        message.setMessage(messageDto.getMessage());
        when(messageDao.update(message)).thenReturn(message);
        when(modelMapper.map(message, MainMessageDto.class)).thenReturn(getMainMessageDtos().get(1));

        WebSocketMessageDto webSocketMessageDtoResult = messageService.updateMessage(messageDto, messageId, chatId, userId);

        assertEquals(webSocketMessageDto, webSocketMessageDtoResult);
    }

    @Test
    public void throwUserNotSenderExceptionTest() {
        int messageId = 2;
        int userId = 1;
        MessageDto messageDto = getMessageDtos().get(0);
        Message message = getMessages().get(1);

        when(messageDao.getById(messageId)).thenReturn(message);

        assertThrows(UserNotSenderException.class, () -> messageService.update(messageId, messageDto, userId));
    }

    @Test
    public void getAllMessagesByChatIdTest() {
        int chatId = 1;
        int userId = 1;

        when(chatDao.getById(chatId)).thenReturn(getChats().get(1));

        List<MainMessageDto> mainMessageDtos = messageService.getAllByChatId(chatId, userId);

        assertTrue(mainMessageDtos.isEmpty());

    }

    public List<Message> getMessages() {
        Message message1 = Message
                .builder()
                .id(1)
                .message("Hello")
                .chat(getChats().get(0))
                .sender(getUsers().get(0))
                .sendingDate(LocalDateTime.now())
                .build();
        Message message2 = Message
                .builder()
                .id(2)
                .message("Hi")
                .chat(getChats().get(0))
                .sender(getUsers().get(1))
                .sendingDate(LocalDateTime.now())
                .build();
        Message message3 = Message
                .builder()
                .id(3)
                .message("Hello")
                .chat(null)
                .sender(getUsers().get(0))
                .sendingDate(LocalDateTime.now())
                .build();
        return new ArrayList<>(Arrays.asList(message1, message2, message3));
    }

    public List<MainMessageDto> getMainMessageDtos() {
        MainMessageDto mainMessageDto1 = MainMessageDto
                .builder()
                .id(1)
                .chatId(1)
                .sender(getMessageUserDtos().get(0))
                .sendingDate("2000-11-11")
                .message("Hello")
                .build();
        MainMessageDto mainMessageDto2 = MainMessageDto
                .builder()
                .id(1)
                .chatId(1)
                .sender(getMessageUserDtos().get(1))
                .sendingDate("2000-12-11")
                .message("Hi")
                .build();
        return new ArrayList<>(Arrays.asList(mainMessageDto1, mainMessageDto2));
    }

    public List<MessageDto> getMessageDtos() {
        MessageDto messageDto1 = MessageDto
                .builder()
                .message("Hello")
                .build();
        MessageDto messageDto2 = MessageDto
                .builder()
                .message("Hi")
                .build();
        return new ArrayList<>(Arrays.asList(messageDto1, messageDto2));
    }

    public List<MessageUserDto> getMessageUserDtos() {
        MessageUserDto messageUserDto1 = MessageUserDto
                .builder()
                .id(1)
                .name("Ivan")
                .photo(null)
                .surname("Petrov")
                .build();

        MessageUserDto messageUserDto2 = MessageUserDto
                .builder()
                .id(2)
                .name("Oleg")
                .photo(null)
                .surname("Ivanov")
                .build();
        MessageUserDto messageUserDto3 = MessageUserDto
                .builder()
                .id(3)
                .name("Olexii")
                .photo(null)
                .surname("Ivanov")
                .build();
        return new ArrayList<>(Arrays.asList(messageUserDto1, messageUserDto2, messageUserDto3));
    }

    public List<User> getUsers() {
        User user1 = User
                .builder()
                .id(1)
                .name("Ivan")
                .surname("Petrov")
                .build();
        User user2 = User
                .builder()
                .userChats(null)
                .id(2)
                .name("Oleg")
                .surname("Ivanov")
                .build();
        User user3 = User
                .builder()
                .id(3)
                .userChats(null)
                .name("Olexsii")
                .surname("Ivanov")
                .build();
        return new ArrayList<>(Arrays.asList(user1, user2, user3));
    }

    public List<Chat> getChats() {
        Chat chat1 = Chat
                .builder()
                .id(1)
                .users(new HashSet<>(Arrays.asList(getUsers().get(0), getUsers().get(2))))
                .chatMessages(new ArrayList<>())
                .build();
        Chat chat2 = Chat
                .builder()
                .id(2)
                .users(new HashSet<>(Arrays.asList(getUsers().get(0), getUsers().get(1))))
                .chatMessages(new ArrayList<>())
                .build();
        return new ArrayList<>(Arrays.asList(chat1, chat2));
    }

}
