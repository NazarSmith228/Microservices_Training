package com.epam.spsa.controller;

import com.epam.spsa.dto.message.MainChatDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.ChatService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
@Api(tags = "Chat")
public class ChatController {

    private final ChatService chatService;

    @PostMapping("/chats/create/users/{firstParticipantId}/{secondParticipantId}")
    @ApiOperation(value = "Create a chat between 2 user by id")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Chat created", response = MainChatDto.class),
            @ApiResponse(code = 400, message = "Validation error", response = ApiError.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiError.class)
    })
    private MainChatDto save(@PathVariable int firstParticipantId,
                             @PathVariable int secondParticipantId) {
        return chatService.save(firstParticipantId, secondParticipantId);
    }

    @GetMapping("/chats/{id}")
    @ApiOperation(value = "Get chat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chat found", response = MainChatDto.class),
            @ApiResponse(code = 404, message = "Chat not found", response = ApiError.class)
    })
    private MainChatDto getById(@PathVariable int id) {
        return chatService.getById(id);
    }

    @DeleteMapping("/chats/{id}")
    @ApiOperation(value = "Delete chat by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chat found", response = MainChatDto.class),
            @ApiResponse(code = 404, message = "Chat not found", response = ApiError.class)
    })
    private void delete(@PathVariable int id) {
        chatService.delete(id);
    }

    @GetMapping("/users/{userId}/chats")
    @ApiOperation(value = "Get all chats by user`s id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Chats found", response = MainChatDto.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiError.class)
    })
    private List<MainChatDto> getAllByUserId(@PathVariable int userId) {
        return chatService.getAllByUserId(userId);
    }

}
