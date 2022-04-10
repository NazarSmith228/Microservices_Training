package com.epam.spsa.controller;

import com.epam.spsa.controller.builder.ArticleCommentBuilder;
import com.epam.spsa.dto.article.ArticleCommentDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.imageBuilder.FileBuilder;
import com.epam.spsa.service.ArticleCommentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.sql.Timestamp;
import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ArticleCommentController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class ArticleCommentControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ArticleCommentService service;

    private MainArticleCommentDto getMainComment(int id) {
        return MainArticleCommentDto.builder()
                .id(id)
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .content("))")
                .image("https:..")
                .likes(3)
                .dislikes(2)
                .build();
    }

    private ArticleCommentDto getComment() {
        return ArticleCommentDto.builder()
                .content("My content")
                .build();
    }

    @Test
    public void getAllTest() throws Exception {
        Mockito.when(service.getAll()).thenAnswer((Answer<List<MainArticleCommentDto>>) invocationOnMock -> {
            List<MainArticleCommentDto> list = new ArrayList<>();
            list.add(getMainComment(1));
            list.add(getMainComment(2));
            return list;
        });

        mockMvc.perform(get("/api/v1/blogs/comments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2));
    }

    @Test
    public void getAllByArticleIdTest() throws Exception {
        int articleId = 1;
        int type = 0;

        Mockito.when(service.getAllByArticleId(articleId, type)).thenAnswer((Answer<List<MainArticleCommentDto>>) invocationOnMock -> {
            List<MainArticleCommentDto> list = new ArrayList<>();
            list.add(getMainComment(1));
            list.add(getMainComment(2));
            return list;
        });

        mockMvc.perform(get("/api/v1/blogs/{id}/comments", articleId)
                .param("type", type + ""))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[1].id").value(2));
    }

    @Test
    public void getCommentTest() throws Exception {
        int commentId = 1;

        Mockito.when(service.getComment(commentId))
                .thenAnswer((Answer<MainArticleCommentDto>)
                        invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(invocationOnMock.getArgument(0)));

        mockMvc.perform(get("/api/v1/blogs/comments/{id}", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void deleteCommentByIdTest() throws Exception {
        int commentId = 1;
        Mockito.doNothing().when(service).deleteComment(commentId);

        mockMvc.perform(delete("/api/v1/blogs/comments/{id}", commentId))
                .andExpect(status().isOk());
    }

    @Test
    public void saveCommentWithImage() throws Exception {
        int blogId = 1;
        int userId = 1;
        ArticleCommentDto commentDto = getComment();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(service.saveCommentWithImage(multipartFile, commentDto, userId, blogId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(1));

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/v1/blogs/{blogId}/comments-img/user/{userId}", blogId, userId)
                .file(multipartFile)
                .param("commentDto", commentDto.getContent()))
                 .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void saveCommentTest() throws Exception {
        int blogId = 1;
        int userId = 1;
        ArticleCommentDto commentDto = getComment();

        Mockito.when(service.saveComment(commentDto, userId, blogId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(1));

        mockMvc.perform(post("/api/v1/blogs/{blogId}/comments/user/{userId}", blogId, userId)
                .param("commentDto", commentDto.getContent()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void saveReplyCommentWithImageTest() throws Exception {
        int commentId = 1;
        int userId = 1;
        ArticleCommentDto commentDto = getComment();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(service.saveReplyCommentWithImage(multipartFile, commentDto, userId, commentId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(1));

        mockMvc.perform(MockMvcRequestBuilders
                .multipart("/api/v1/blogs/reply-comments-img/{commentId}/user/{userId}", commentId, userId)
                .file(multipartFile)
                .param("commentDto", commentDto.getContent()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void saveReplyComment() throws Exception {
        int commentId = 1;
        int userId = 1;
        ArticleCommentDto commentDto = getComment();

        Mockito.when(service.saveReplyComment(commentDto, userId, commentId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(1));

        mockMvc.perform(post("/api/v1/blogs/reply-comments/{commentId}/user/{userId}", commentId, userId)
                .param("commentDto", commentDto.getContent()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }


    @Test
    public void updateCommentWithImageTest() throws Exception {
        int commentId = 1;
        ArticleCommentDto commentDto = getComment();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(service.updateCommentWithImage(multipartFile, commentDto, commentId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(1));

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/blogs/comments-img/{commentId}", commentId);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(multipartFile)
                .param("commentDto", commentDto.getContent()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void updateCommentTest() throws Exception {
        int commentId = 1;
        ArticleCommentDto commentDto = getComment();

        Mockito.when(service.updateComment(commentDto, commentId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> ArticleCommentBuilder.getMainArticleCommentDto(1));

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/blogs/comments/{commentId}", commentId);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .param("commentDto", commentDto.getContent()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void updateCommentIncTest() throws Exception {
        int commentId = 1;

        Mockito.when(service.likeComment(commentId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> {
                    MainArticleCommentDto mainArticleCommentDto = ArticleCommentBuilder.getMainArticleCommentDto(1);
                    mainArticleCommentDto.setLikes(3);
                    mainArticleCommentDto.setDislikes(2);
                    return mainArticleCommentDto;
                });

        mockMvc.perform(post("/api/v1/blogs/comments/{commentId}/inc", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }

    @Test
    public void updateCommentDecTest() throws Exception {
        int commentId = 1;

        Mockito.when(service.dislikeComment(commentId))
                .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> {
                    MainArticleCommentDto mainArticleCommentDto = ArticleCommentBuilder.getMainArticleCommentDto(1);
                    mainArticleCommentDto.setLikes(3);
                    mainArticleCommentDto.setDislikes(2);
                    return mainArticleCommentDto;
                });

        mockMvc.perform(post("/api/v1/blogs/comments/{commentId}/dec", commentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.content").value("My content"))
                .andExpect(jsonPath("$.image").value("https:.."))
                .andExpect(jsonPath("$.likes").value(3))
                .andExpect(jsonPath("$.dislikes").value(2));
    }


}
