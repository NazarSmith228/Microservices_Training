package com.epam.spsa.controller;

import com.epam.spsa.dto.message.MainMessageDto;
import com.epam.spsa.dto.message.MessageDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.MessageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/messages")
@RequiredArgsConstructor
@Api(tags = "Message")
public class MessageController {

    private final MessageService messageService;

    private final SimpMessagingTemplate template;

    @PostMapping("/send/chats/{chatId}/user/{userId}")
    @ApiOperation(value = "Write a message to chat")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Message posted", response = MainMessageDto.class),
            @ApiResponse(code = 400, message = "Validation error or user is not a participant of this chat", response = ApiError.class),
            @ApiResponse(code = 404, message = "User or chat not found", response = ApiError.class)
    })
    public MainMessageDto save(@RequestBody MessageDto messageDto, @PathVariable int chatId, @PathVariable int userId) {
        return messageService.save(messageDto, chatId, userId);
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Get message by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message found", response = MainMessageDto.class),
            @ApiResponse(code = 404, message = "Message not found", response = ApiError.class)
    })
    private MainMessageDto getById(@PathVariable int id) {
        return messageService.getById(id);
    }

    @DeleteMapping("/delete/{messageId}/user/{userId}")
    @ApiOperation(value = "Delete message by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message deleted"),
            @ApiResponse(code = 400, message = "User is not a participant of this chat", response = ApiError.class),
            @ApiResponse(code = 404, message = "Message not found", response = ApiError.class)
    })
    private void delete(@PathVariable int messageId, @PathVariable int userId) {
        messageService.delete(messageId, userId);
    }

    @PutMapping("/update/{messageId}/user/{userId}")
    @ApiOperation(value = "Update message by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Message updated", response = MainMessageDto.class),
            @ApiResponse(code = 400, message = "Validation error or user is not a participant of this chat", response = ApiError.class),
            @ApiResponse(code = 404, message = "Message not found", response = ApiError.class)
    })
    private MainMessageDto update(@PathVariable int messageId, @PathVariable int userId,
                                  @RequestBody MessageDto messageDto) {
        return messageService.update(messageId, messageDto, userId);
    }

    @GetMapping("/chats/{chatId}/user/{userId}")
    @ApiOperation(value = "Get all messages from chat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chat found", response = MainMessageDto.class),
            @ApiResponse(code = 400, message = "User is not a participant of this chat", response = ApiError.class),
            @ApiResponse(code = 404, message = "Chat not found", response = ApiError.class)
    })
    private List<MainMessageDto> getAllByChatId(@PathVariable int chatId, @PathVariable int userId) {
        return messageService.getAllByChatId(chatId, userId);
    }

    @MessageMapping("/send/chats/{chatId}/user/{userId}")
    private void sendMessageToChat(@DestinationVariable int chatId, @DestinationVariable int userId,
                                   @Payload MessageDto messageDto) {
        template.convertAndSend("/spsa/chat/" + chatId, messageService.save(messageDto, chatId, userId));
    }

    @MessageMapping("/update/{messageId}/chats/{chatId}/user/{userId}")
    private void updateMessageInChat(@DestinationVariable int messageId, @DestinationVariable int chatId,
                                     @DestinationVariable int userId, @Payload MessageDto messageDto) {
        template.convertAndSend("/spsa/chat/" + chatId, messageService.updateMessage(messageDto, messageId, chatId, userId));
    }

    @MessageMapping("/delete/{messageId}/chats/{chatId}/user/{userId}")
    private void deleteMessageInChat(@DestinationVariable int messageId, @DestinationVariable int chatId,
                                     @DestinationVariable int userId) {
        template.convertAndSend("/spsa/chat/" + chatId, messageService.deleteMessage(messageId, chatId, userId));
    }

}
