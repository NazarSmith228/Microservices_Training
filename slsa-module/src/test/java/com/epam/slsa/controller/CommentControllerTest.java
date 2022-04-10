package com.epam.slsa.controller;

import com.epam.slsa.dto.comment.CommentDto;
import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.comment.UpdateCommentDto;
import com.epam.slsa.dto.pagination.PaginationDto;
import com.epam.slsa.service.CommentService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CommentService commentService;

    private CommentDto commentDto;

    private UpdateCommentDto updateCommentDto;

    private MainCommentDto mainCommentDto;

    @BeforeEach
    public void setUpVariables() {
        updateCommentDto = UpdateCommentDto.builder()
                .rating(3)
                .comment("hihihihihihihi").build();
        commentDto = CommentDto.builder()
                .userId(1)
                .rating(3)
                .comment("hihihihihihihi").build();
        mainCommentDto = MainCommentDto.builder()
                .id(1)
                .coachId(3)
                .creationDate(new Timestamp(System.currentTimeMillis()).toString())
                .userId(1)
                .rating(3)
                .comment("hihihihihihihi").build();
    }

    @Test
    public void saveCommentTest() throws Exception {
        int coachId = 3;
        when(commentService.save(commentDto, coachId)).thenReturn(mainCommentDto);

        mockMvc.perform(post("/api/v1/coaches/{coachId}/comment", coachId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.coachId").value(coachId));
    }

    @Test
    public void getAllCommentsTest() throws Exception {
        int coachId = 3;
        String page = "1";
        String size = "2";
        List<MainCommentDto> mainTagDtoList = new ArrayList<>(Collections.singletonList(mainCommentDto));
        PaginationDto<MainCommentDto> paginationDto = PaginationDto.<MainCommentDto>builder()
                .entities(mainTagDtoList)
                .entitiesLeft(0)
                .quantity(2)
                .build();
        when(commentService.getAllByCoachId(anyInt(), anyInt(),  anyInt()))
                .thenReturn(paginationDto);

        mockMvc.perform(get("/api/v1/coaches/{coachId}/comments", coachId)
                .param("page", page)
                .param("size", size)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }


    @Test
    public void deleteTagTest() throws Exception {
        int id = 3;

        doNothing()
                .when(commentService)
                .delete(id, id);

        mockMvc.perform(delete("/api/v1/coaches/{coachId}/comments/{commentId}", id, id))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTagByUserIdTest() throws Exception {
        int id = 3;

        doNothing()
                .when(commentService)
                .deleteByUserId(id);

        mockMvc.perform(delete("/api/v1/coaches/comments/user/{userId}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void updateCommentTest() throws Exception {

        when(commentService.update(any(UpdateCommentDto.class), anyInt(), anyInt()))
                .thenReturn(mainCommentDto);

        mockMvc.perform(put("/api/v1/coaches/{coachId}/comments/{commentId}", 3, 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateCommentDto))
                .characterEncoding("UTF-8"))
                .andExpect(status().isOk());
    }

}
