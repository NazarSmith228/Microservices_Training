package com.epam.slsa.dao;

import com.epam.slsa.model.Coach;
import com.epam.slsa.model.Comment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.epam.slsa.builders.coach.CoachDtoBuilder.getCoach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@SpringBootTest
@Transactional
@Rollback
public class CommentDaoTest {

    @Autowired
    private CommentDao commentDao;

    @Autowired
    private CoachDao coachDao;

    private Coach newCoach;

    @BeforeEach
    public void setUpVariables() {
        Coach coach = getCoach();
        newCoach = coachDao.save(coach);
    }

    @Test
    public void saveTest() {
        Comment comment = getComment();
        Comment newComment = commentDao.save(comment);

        assertEquals(comment, newComment);
    }

    @Test
    public void updateTest() {
        Comment comment = getComment();
        Comment newComment = commentDao.save(comment);
        newComment.setComment(")))))");
        Comment updateComment = commentDao.update(newComment);

        assertEquals(updateComment, newComment);
    }

    @Test
    public void deleteTest() {
        Comment comment = getComment();
        Comment newComment = commentDao.save(comment);
        int id = newComment.getId();
        commentDao.delete(newComment);

        assertNull(commentDao.getById(id));
    }

    @Test
    public void getByIdTest() {
        Comment comment = getComment();
        Comment newComment = commentDao.save(comment);
        int id = newComment.getId();
        Comment getByIdComment = commentDao.getById(id);

        assertEquals(newComment, getByIdComment);
    }

    @Test
    public void getAllTest() {
        Comment comment = getComment();
        int sizeBefore = commentDao.getAll().size();
        commentDao.save(comment);
        int sizeAfter = commentDao.getAll().size();

        assertEquals(1, sizeAfter - sizeBefore);
    }

    private Comment getComment() {
        return Comment.builder()
                .id(0)
                .coach(newCoach)
                .comment("Nice coach")
                .creationDate(LocalDateTime.now())
                .rating(3)
                .userId(2)
                .build();
    }

}
