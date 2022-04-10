package com.epam.slsa.service.impl;

import com.epam.slsa.dao.CoachDao;
import com.epam.slsa.dao.CommentDao;
import com.epam.slsa.dto.comment.CommentDto;
import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.comment.UpdateCommentDto;
import com.epam.slsa.dto.pagination.PaginationDto;
import com.epam.slsa.error.exception.EntityAlreadyExistsException;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.model.Coach;
import com.epam.slsa.model.Comment;
import com.epam.slsa.service.CommentService;
import com.epam.slsa.utils.PaginationUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.max;

@RequiredArgsConstructor
@Service
@PropertySource(value = "classpath:/messages.properties")
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CoachDao coachDao;

    private final CommentDao commentDao;

    private final ModelMapper mapper;

    private final PartnerClient partnerClient;

    @Value("${coach.exception.notfound}")
    private String coachExceptionMessage;

    @Value("${user.exception.notfound}")
    private String userExceptionMessage;

    @Value("${comment.exception.notfound}")
    private String commentExceptionMessage;

    @Value("${comment.exists.exception}")
    private String commentAlreadyExistsExceptionMessage;

    @Value("${comment.notbelong.coach}")
    private String commentNotBelongToCoachMessage;

    @Override
    public MainCommentDto save(CommentDto commentDto, int coachId) {
        log.info("Saving CommentDto: {}", commentDto);
        Coach coach = getCoachById(coachId);

        int userId = commentDto.getUserId();
        isUserExists(userId);

        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setId(0);
        comment.setCreationDate(LocalDateTime.now());
        comment.setCoach(coach);
        try {
            comment = commentDao.save(comment);
        } catch (DataIntegrityViolationException e) {
            throw new EntityAlreadyExistsException(commentAlreadyExistsExceptionMessage);
        }

        updateRating(coach, comment);

        return mapAndSetUserInfo(comment);
    }

    private void isUserExists(int userId) {
        try {
            partnerClient.getUserById(userId);
        } catch (FeignException.NotFound e) {
            log.error("User wasn't found. id: {}", userId);
            throw new EntityNotFoundException(userExceptionMessage + userId);
        }
    }

    private void updateRating(Coach coach, Comment comment) {
        log.info("Update rating of coach");
        int commentSize = coach.getComments().size();
        double rating = coach.getRating();
        coach.setRating((rating * (commentSize - 1) + comment.getRating()) / (commentSize));
        coachDao.update(coach);
    }

    private void updateRatingAfterDelete(Comment comment) {
        log.info("Update rating of coach after Delete");
        Coach coach = comment.getCoach();
        int commentSize = coach.getComments().size();
        if (commentSize - 1 == 0) {
            coach.setRating(0);
        } else {
            double rating = coach.getRating();
            coach.setRating((rating * commentSize - comment.getRating()) / (max(commentSize - 1, 1)));
        }
        coachDao.update(coach);
    }

    private void updateRatingAfterUpdate(Comment comment, int newRating) {
        log.info("Update rating of coach after Update");
        Coach coach = comment.getCoach();
        int commentSize = coach.getComments().size();
        double rating = coach.getRating();
        coach.setRating((rating * commentSize - comment.getRating() + newRating) / commentSize);
        coachDao.update(coach);
    }

    @Override
    public void delete(int coachId, int commentId) {
        Comment comment = getCommentById(commentId);
        if (comment.getCoach().getId() != coachId) {
            throw new EntityNotFoundException(commentNotBelongToCoachMessage + coachId);
        }
        updateRatingAfterDelete(comment);
        commentDao.delete(comment);
        Coach coach = comment.getCoach();
        coach.getComments().remove(comment);
        coachDao.update(coach);
    }

    @Override
    public PaginationDto<MainCommentDto> getAllByCoachId(int coachId, int pageNumber, int pageSize) {
        Coach coach = getCoachById(coachId);
        List<MainCommentDto> comments = coach.getComments().stream()
                .map(this::mapAndSetUserInfo)
                .sorted(Comparator.comparing(MainCommentDto::getCreationDate))
                .collect(Collectors.toList());
        return PaginationUtils.paginate(comments, pageNumber, pageSize);
    }

    @Override
    public MainCommentDto update(UpdateCommentDto editedCommentDto, int coachId, int commentId) {
        Comment oldComment = getCommentById(commentId);
        if (oldComment.getCoach().getId() != coachId) {
            throw new EntityNotFoundException(commentNotBelongToCoachMessage + coachId);
        }
        updateRatingAfterUpdate(oldComment, editedCommentDto.getRating());
        mapper.map(editedCommentDto, oldComment);
        oldComment.setCreationDate(LocalDateTime.now());
        return mapper.map(commentDao.update(oldComment), MainCommentDto.class);
    }

    @Override
    public void deleteByUserId(int userId) {
        commentDao.getAll().stream()
                .filter(comment -> comment.getUserId() == userId)
                .forEach(comment -> deleteCommentsBelongToUser(comment.getId()));
    }

    public void deleteCommentsBelongToUser(int commentId) {
        Comment comment = getCommentById(commentId);
        updateRatingAfterDelete(comment);
        commentDao.delete(comment);
        Coach coach = comment.getCoach();
        coach.getComments().remove(comment);
        coachDao.update(coach);
    }

    private Comment getCommentById(int id) {
        log.info("Getting Comment by id: {}", id);
        Comment comment = commentDao.getById(id);
        if (comment == null) {
            log.error("Coach wasn't found. id: {}", id);
            throw new EntityNotFoundException(commentExceptionMessage + id);
        }
        log.debug("Result Comment: {}", comment);
        return comment;
    }

    private Coach getCoachById(int id) {
        log.info("Getting Coach by id: {}", id);
        Coach coach = coachDao.getById(id);
        if (coach == null) {
            log.error("Coach wasn't found. id: {}", id);
            throw new EntityNotFoundException(coachExceptionMessage + id);
        }
        return coach;
    }

    private MainCommentDto mapAndSetUserInfo(Comment comment) {
        MainCommentDto commentDto = mapper.map(comment, MainCommentDto.class);
        commentDto.setUser(partnerClient.getUserById(commentDto.getUserId()));
        return commentDto;
    }

}
