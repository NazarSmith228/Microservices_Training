package com.epam.spsa.controller;

import com.epam.spsa.controller.builder.MessagingBuilder;
import com.epam.spsa.dto.message.MainMessageDto;
import com.epam.spsa.dto.message.MessageDto;
import com.epam.spsa.service.MessageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = MessageController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class MessageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MessageService messageService;

    @MockBean
    private SimpMessagingTemplate messagingTemplate;

    @Test
    public void saveMessageTest() throws Exception {
        MainMessageDto mainMessageDto = MessagingBuilder.getMainMessageDto();
        MessageDto messageDto = MessagingBuilder.getMessageDto();
        int userId = 1;
        int chatId = 1;


        when(messageService.save(any(), anyInt(), anyInt()))
                .thenReturn(mainMessageDto);

        mockMvc.perform(post("/api/v1/messages/send/chats/{chatId}/user/{userId}", chatId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void getMessageByIdTest() throws Exception {
        MainMessageDto mainMessageDto = MessagingBuilder.getMainMessageDto();
        int messageId = 1;

        when(messageService.getById(anyInt()))
                .thenReturn(mainMessageDto);

        mockMvc.perform(get("/api/v1/messages/{id}", messageId)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteMessageTest() throws Exception {
        int messageId = 1;
        int userId = 1;

        doNothing().when(messageService).delete(messageId, userId);

        mockMvc.perform(delete("/api/v1/messages/delete/{messageId}/user/{userId}", messageId, userId)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

    @Test
    public void updateMessageTest() throws Exception {
        MainMessageDto mainMessageDto = MessagingBuilder.getMainMessageDto();
        MessageDto messageDto = MessagingBuilder.getMessageDto();
        int messageId = 1;
        int userId = 1;

        when(messageService.update(anyInt(), any(), anyInt()))
                .thenReturn(mainMessageDto);

        mockMvc.perform(put("/api/v1/messages/update/{messageId}/user/{userId}",
                messageId, userId, messageDto)
                .characterEncoding("UTF-8")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(messageDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllMessagesTest() throws Exception {
        MainMessageDto mainMessageDto = MessagingBuilder.getMainMessageDto();
        int chatId = 1;
        int userId = 1;

        when(messageService.getAllByChatId(anyInt(), anyInt()))
                .thenReturn(Collections.singletonList(mainMessageDto));

        mockMvc.perform(get("/api/v1/messages/chats/{chatId}/user/{userId}", chatId, userId)
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

}
