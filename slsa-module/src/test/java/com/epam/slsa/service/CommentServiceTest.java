package com.epam.slsa.service;

import com.epam.slsa.dao.CoachDao;
import com.epam.slsa.dao.CommentDao;
import com.epam.slsa.dto.comment.CommentDto;
import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.comment.UpdateCommentDto;
import com.epam.slsa.dto.pagination.PaginationDto;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.model.Coach;
import com.epam.slsa.model.Comment;
import com.epam.slsa.service.impl.CommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoach;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CommentServiceTest {

    @Mock
    private CoachDao coachDao;

    @Mock
    private CommentDao commentDao;

    @Mock
    private PartnerClient partnerClient;

    @InjectMocks
    private CommentServiceImpl commentService;

    @Mock
    private ModelMapper mapper;

    private CommentDto commentDto;

    private UpdateCommentDto updateCommentDto;

    private MainCommentDto mainCommentDto;

    @BeforeEach
    public void setUpVariables() {
        commentDto = CommentDto.builder()
                .userId(1)
                .rating(3)
                .comment("hihihihihihihi").build();

        updateCommentDto = UpdateCommentDto.builder()
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
    public void saveTest() {
        int coachId = 2;
        Coach coach = getCoach();
        coach.setComments(new ArrayList<>());
        Comment comment = Comment.builder()
                .userId(1)
                .rating(3)
                .creationDate(LocalDateTime.now())
                .comment("gjfksjgkdsfjgkf")
                .coach(coach)
                .id(1).build();

        when(coachDao.getById(anyInt())).thenReturn(coach);

        when(partnerClient.getUserById(anyInt())).thenReturn(MainUserDto.builder()
                .id(2)
                .email("kjskfj@sfjk.com")
                .name("Yura")
                .build());

        when(mapper.map(commentDto, Comment.class))
                .thenReturn(comment);

        when(commentDao.save(any(Comment.class))).thenReturn(comment);

        when(mapper.map(comment, MainCommentDto.class)).thenReturn(mainCommentDto);

        when(coachDao.update(any(Coach.class))).thenReturn(coach);

        MainCommentDto mainCommentDto1 = commentService.save(commentDto, coachId);

        Assertions.assertEquals(mainCommentDto, mainCommentDto1);
    }

    @Test
    public void getAllCommentByCoachId() {
        Coach coach = getCoach();
        Comment comment = Comment.builder()
                .userId(1)
                .rating(3)
                .creationDate(LocalDateTime.now())
                .comment("gjfksjgkdsfjgkf")
                .coach(coach)
                .id(1).build();
        coach.setComments(new ArrayList<>(Collections.singletonList(comment)));
        int coachId = 1;

        when(coachDao.getById(anyInt())).thenReturn(coach);

        when(mapper.map(comment, MainCommentDto.class)).thenReturn(mainCommentDto);

        PaginationDto paginationDto = commentService.getAllByCoachId(coachId, 1, 2);

        Assertions.assertEquals(mainCommentDto, paginationDto.getEntities().get(0));
    }

    @Test
    public void updateCommentTest() {
        Coach coach = getCoach();
        coach.setComments(new ArrayList<>());
        int id = 1;
        Comment comment = Comment.builder()
                .userId(1)
                .rating(3)
                .creationDate(LocalDateTime.now())
                .comment("gjfksjgkdsfjgkf")
                .coach(coach)
                .id(1).build();
        when(commentDao.getById(id)).thenReturn(comment);

        when(mapper.map(commentDto, Comment.class)).thenReturn(comment);

        when(commentDao.update(comment)).thenReturn(comment);

        when(coachDao.update(any(Coach.class))).thenReturn(comment.getCoach());

        when(mapper.map(comment, MainCommentDto.class)).thenReturn(mainCommentDto);

        MainCommentDto mainCommentDto1 = commentService.update(updateCommentDto, coach.getId(), id);

        Assertions.assertEquals(mainCommentDto, mainCommentDto1);
    }

    @Test
    public void deleteCommentTest() {
        int id = 1;
        Coach coach = getCoach();
        coach.setComments(new ArrayList<>());
        Comment comment = Comment.builder()
                .userId(1)
                .rating(3)
                .creationDate(LocalDateTime.now())
                .comment("gjfksjgkdsfjgkf")
                .coach(coach)
                .id(1).build();

        when(commentDao.getById(id)).thenReturn(comment);

        when(coachDao.update(any(Coach.class))).thenReturn(comment.getCoach());

        Mockito.doNothing().when(commentDao).delete(any(Comment.class));

        Assertions.assertDoesNotThrow(() -> commentService.delete(coach.getId(), id));
    }

}
