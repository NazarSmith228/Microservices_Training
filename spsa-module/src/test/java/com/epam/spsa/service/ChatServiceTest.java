package com.epam.spsa.service;

import com.epam.spsa.dao.ChatDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.message.MainChatDto;
import com.epam.spsa.dto.message.MessageUserDto;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.SameParticipantException;
import com.epam.spsa.model.Chat;
import com.epam.spsa.model.User;
import com.epam.spsa.service.impl.ChatServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class ChatServiceTest {

    @InjectMocks
    private ChatServiceImpl chatService;

    @Mock
    private ChatDao chatDao;

    @Mock
    private UserDao userDao;

    @Mock
    private ModelMapper mapper;

    @Test
    public void saveTest() {
        int firstId = 1;
        int secondId = 2;

        when(userDao.getById(firstId)).thenReturn(getUsers().get(0));
        when(userDao.getById(secondId)).thenReturn(getUsers().get(1));
        when(chatDao.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(getChats().get(0))));
        when(chatDao.save(any())).thenReturn(getChats().get(1));
        when(mapper.map(Mockito.any(Chat.class), Mockito.eq(MainChatDto.class))).thenReturn(getMainChatDtos().get(1));

        MainChatDto mainChatDtoResult = chatService.save(firstId, secondId);

        assertEquals(getMainChatDtos().get(1), mainChatDtoResult);
    }

    @Test
    public void throwSameParticipantExceptionTest() {
        int firstId = 1;

        assertThrows(SameParticipantException.class, () -> chatService.save(firstId, firstId));
    }

    @Test
    public void throwEntityAlreadyExistsExceptionTest() {
        int firstId = 1;
        int secondId = 2;

        when(userDao.getById(firstId)).thenReturn(getUsers().get(0));
        when(userDao.getById(secondId)).thenReturn(getUsers().get(1));
        when(chatDao.getAll()).thenReturn(new ArrayList<>(Collections.singletonList(getChats().get(1))));

        assertThrows(EntityAlreadyExistsException.class, () -> chatService.save(firstId, secondId));
    }

    @Test
    public void getChatByIdTest() {
        int chatId = 1;

        when(chatDao.getById(chatId)).thenReturn(getChats().get(0));
        when(mapper.map(Mockito.any(Chat.class), Mockito.eq(MainChatDto.class))).thenReturn(getMainChatDtos().get(0));

        MainChatDto mainChatDto = chatService.getById(chatId);

        assertEquals(getMainChatDtos().get(0), mainChatDto);
    }

    @Test
    public void deleteChatTest() {
        int chatId = 2;

        when(chatDao.getById(chatId)).thenReturn(getChats().get(1));
        doNothing().when(chatDao).delete(getChats().get(1));

        assertDoesNotThrow(() -> chatService.delete(chatId));
    }

    @Test
    public void deleteChatThrowExceptionTest() {
        int chatId = 2;

        when(chatDao.getById(chatId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> chatService.delete(chatId));
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
                .id(2)
                .name("Oleg")
                .surname("Ivanov")
                .build();
        User user3 = User
                .builder()
                .id(3)
                .name("Olexsii")
                .surname("Ivanov")
                .build();
        return new ArrayList<>(Arrays.asList(user1, user2, user3));
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

    public List<MainChatDto> getMainChatDtos() {
        MainChatDto mainChatDto1 = MainChatDto
                .builder()
                .id(1)
                .chatMessages(null)
                .users(new HashSet<>(Arrays.asList(getMessageUserDtos().get(0), getMessageUserDtos().get(2))))
                .build();
        MainChatDto mainChatDto2 = MainChatDto
                .builder()
                .id(2)
                .chatMessages(null)
                .users(new HashSet<>(Arrays.asList(getMessageUserDtos().get(0), getMessageUserDtos().get(1))))
                .build();
        return new ArrayList<>(Arrays.asList(mainChatDto1, mainChatDto2));
    }
}
