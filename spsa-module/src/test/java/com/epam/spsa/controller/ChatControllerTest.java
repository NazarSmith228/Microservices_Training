package com.epam.spsa.controller;

import com.epam.spsa.dto.message.MainChatDto;
import com.epam.spsa.dto.message.MessageUserDto;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.SameParticipantException;
import com.epam.spsa.service.ChatService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChatController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class ChatControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChatService service;

    @Test
    public void saveChatTest() throws Exception {
        int firstUserId = 1;
        int secondUserId = 2;

        when(service.save(firstUserId, secondUserId)).thenReturn(getMainChatDto());

        mockMvc.perform(post("/api/v1/chats/create/users/" + firstUserId + "/" + secondUserId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getMainChatDto().getId()));
    }

    @Test
    public void throwSameParticipantExceptionTest() throws Exception {
        int firstUserId = 1;

        when(service.save(firstUserId, firstUserId)).thenThrow(SameParticipantException.class);

        mockMvc.perform(post("/api/v1/chats/create/users/" + firstUserId + "/" + firstUserId))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void throwEntityAlreadyExistsExceptionTest() throws Exception {
        int firstUserId = 1;

        when(service.save(firstUserId, firstUserId)).thenThrow(EntityAlreadyExistsException.class);

        mockMvc.perform(post("/api/v1/chats/create/users/" + firstUserId + "/" + firstUserId))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getChatByIdTest() throws Exception {
        int chatId = 1;
        when(service.getById(chatId)).thenReturn(getMainChatDto());

        mockMvc.perform(get("/api/v1/chats/" + chatId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(getMainChatDto().getId()));
    }

    @Test
    public void deleteChatByIdTest() throws Exception {
        int chatId = 1;

        doNothing()
                .when(service)
                .delete(chatId);

        mockMvc.perform(delete("/api/v1/chats/{id}", chatId))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllChatsByUserIdTest() throws Exception {
        int userId = 1;

        when(service.getAllByUserId(userId)).thenReturn(new ArrayList<>(Collections.singletonList(getMainChatDto())));

        mockMvc.perform(get("/api/v1/users/" + userId + "/chats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(getMainChatDto().getId()));
    }

    public MainChatDto getMainChatDto() {
        return MainChatDto.builder()
                .id(1)
                .users(getMessageUserDtos())
                .chatMessages(null)
                .build();
    }

    public Set<MessageUserDto> getMessageUserDtos() {
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
        return new HashSet<>(Arrays.asList(messageUserDto1, messageUserDto2));
    }

}
