package com.epam.spsa.service.impl;

import com.epam.spsa.dao.ArticleCommentDao;
import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.dao.EstimationDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.article.ArticleCommentDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.error.exception.AccessDeniedException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.UserNotParticipantException;
import com.epam.spsa.model.Article;
import com.epam.spsa.model.ArticleComment;
import com.epam.spsa.model.Estimation;
import com.epam.spsa.model.User;
import com.epam.spsa.s3api.S3Manager;
import com.epam.spsa.service.ArticleCommentService;
import com.epam.spsa.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource("classpath:/exceptionMessages.properties")
@Slf4j
@SuppressWarnings("Duplicates")
public class ArticleCommentServiceImpl implements ArticleCommentService {

    private final ArticleCommentDao articleCommentDao;

    private final ArticleDao articleDao;

    private final UserDao userDao;

    private final EstimationDao estimationDao;

    private final UserService userService;

    private final ModelMapper modelMapper;

    private final S3Manager s3Manager;

    @Value("${article.exception.notfound}")
    private String exceptionMessage;

    @Value("${article.comment.exception.notfound}")
    private String exceptionMessage2;

    @Value("${user.exception.notfound}")
    private String exceptionMessage3;

    @Value("${user.comment.notSupported}")
    private String exceptionMessage4;

    @Value("${estimation.not.found}")
    private String exceptionMessage5;

    @Value("${user.auth.notFound}")
    private String exceptionMessage6;

    @Override
    public MainArticleCommentDto getComment(int id) {
        log.info("Getting comment by id: {}", id);
        ArticleComment comment = articleCommentDao.getById(id);
        if (comment == null) {
            throw new EntityNotFoundException(exceptionMessage2 + id);
        }
        return modelMapper.map(comment, MainArticleCommentDto.class);
    }

    @Override
    public List<MainArticleCommentDto> getAll() {
        log.info("Getting List of Comments");
        List<ArticleComment> articleComments = articleCommentDao.getAll();
        return articleComments
                .stream()
                .map(articleComment -> modelMapper.map(articleComment, MainArticleCommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MainArticleCommentDto> getAllByArticleId(int id, int type) {
        log.info("Getting List of Comments by article id: {}", id);
        if (articleDao.getById(id) == null) {
            throw new EntityNotFoundException(exceptionMessage + id);
        }
        List<ArticleComment> list;
        if (type > 0) {
            list = articleCommentDao.getAllByArticleIdDESK(id);
        } else if (type < 0) {
            list = articleCommentDao.getAllByArticleIdASC(id);
        } else {
            list = articleCommentDao.getAllByArticleId(id);
        }

        return list
                .stream()
                .map(articleComment -> modelMapper.map(articleComment, MainArticleCommentDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteComment(int id) {
        log.info("Deleting comment with id: {}", id);
        ArticleComment comment = articleCommentDao.getById(id);
        if (comment == null) {
            throw new EntityNotFoundException(exceptionMessage2 + id);
        }
        comment.setUser(null);
        articleCommentDao.update(comment);

        while (true) {
            ArticleComment comment2 = articleCommentDao.getDeletedComment();
            if (comment2 == null) break;
            List<ArticleComment> comments = comment2.getArticleComments();
            for (ArticleComment articleComment : comments) {
                articleComment.setUser(null);
                articleCommentDao.update(articleComment);
            }
            s3Manager.deleteFileByUserId(comment2.getId(), "blogComments");
            articleCommentDao.deleteComment(comment2.getId());
        }
    }

    @Override
    public MainArticleCommentDto saveCommentWithImage(MultipartFile multipartFile,
                                                      ArticleCommentDto comment,
                                                      int userId, int articleId) {
        ArticleComment newComment = getCommentForSave(userId, articleId, comment);
        newComment.setImage(s3Manager.saveImageById(multipartFile, newComment.getId(), "blogComments"));
        articleCommentDao.update(newComment);

        log.info("Created comment: {}", newComment);
        return modelMapper.map(newComment, MainArticleCommentDto.class);
    }

    @Override
    public MainArticleCommentDto saveComment(ArticleCommentDto comment,
                                             int userId, int articleId) {
        ArticleComment newComment = getCommentForSave(userId, articleId, comment);

        log.info("Created comment: {}", newComment);
        return modelMapper.map(newComment, MainArticleCommentDto.class);
    }

    private ArticleComment getCommentForSave(int userId, int articleId, ArticleCommentDto comment) {
        log.info("Adding new comment with userId: {} and articleId: {}", userId, articleId);
        User user = userDao.getById(userId);
        Article article = articleDao.getById(articleId);
        if (user == null) {
            throw new EntityNotFoundException(exceptionMessage3 + userId);
        } else if (article == null) {
            throw new EntityNotFoundException(exceptionMessage + articleId);
        }

        ArticleComment newComment = modelMapper.map(comment, ArticleComment.class);
        newComment.setCreationDate(new Timestamp(System.currentTimeMillis()));
        newComment.setEstimations(new HashSet<>());
        newComment.setUser(user);
        newComment.setArticle(article);

        articleCommentDao.save(newComment);
        return newComment;
    }

    @Override
    public MainArticleCommentDto saveReplyCommentWithImage(MultipartFile multipartFile,
                                                           ArticleCommentDto comment,
                                                           int userId, int commentId) {
        log.info("Adding new reply comment with userId: {} and commentId: {}", userId, commentId);
        User user = userDao.getById(userId);
        ArticleComment mainComment = articleCommentDao.getById(commentId);
        ArticleComment articleComment = getReplyCommentForSave(comment, userId, commentId, user, mainComment);

        articleCommentDao.save(articleComment);
        articleComment.setImage(s3Manager.saveImageById(multipartFile, articleComment.getId(), "blogComments"));
        articleCommentDao.update(articleComment);

        setNewReplayCom(articleComment, mainComment);

        log.info("Created comment: {}", articleComment);
        return modelMapper.map(articleComment, MainArticleCommentDto.class);
    }

    @Override
    public MainArticleCommentDto saveReplyComment(ArticleCommentDto comment,
                                                  int userId, int commentId) {
        log.info("Adding new reply comment with userId: {} and commentId: {}", userId, commentId);
        User user = userDao.getById(userId);
        ArticleComment mainComment = articleCommentDao.getById(commentId);
        ArticleComment articleComment = getReplyCommentForSave(comment, userId, commentId, user, mainComment);

        articleCommentDao.save(articleComment);
        setNewReplayCom(articleComment, mainComment);

        log.info("Created comment: {}", articleComment);
        return modelMapper.map(articleComment, MainArticleCommentDto.class);
    }

    private ArticleComment getReplyCommentForSave(ArticleCommentDto comment, int userId, int commentId, User user, ArticleComment mainComment) {
        if (user == null) {
            throw new EntityNotFoundException(exceptionMessage3 + userId);
        } else if (mainComment == null) {
            throw new EntityNotFoundException(exceptionMessage2 + commentId);
        }

        ArticleComment articleComment = modelMapper.map(comment, ArticleComment.class);
        articleComment.setCreationDate(new Timestamp(System.currentTimeMillis()));
        articleComment.setUser(user);
        articleComment.setArticle(null);
        return articleComment;
    }

    private void setNewReplayCom(ArticleComment articleComment, ArticleComment mainComment) {
        log.info("Setting new reply comment to main: {}", articleComment);
        List<ArticleComment> articleComments = mainComment.getArticleComments();
        articleComments.add(articleComment);
        mainComment.setArticleComments(articleComments);
        articleCommentDao.update(mainComment);
    }


    @Override
    public MainArticleCommentDto updateCommentWithImage(MultipartFile multipartFile,
                                                        ArticleCommentDto comment,
                                                        int commentId) {
        ArticleComment mainComment = getCommentForUpdate(comment, commentId);

        mainComment.setImage(s3Manager.saveImageById(multipartFile, mainComment.getId(), "blogComments"));
        articleCommentDao.update(mainComment);

        log.info("Updated comment: {}", mainComment);
        return modelMapper.map(mainComment, MainArticleCommentDto.class);
    }

    @Override
    public MainArticleCommentDto updateComment(ArticleCommentDto comment,
                                               int commentId) {
        ArticleComment mainComment = getCommentForUpdate(comment, commentId);
        mainComment.setImage(null);

        s3Manager.deleteFileByUserId(commentId, "blogComments");
        articleCommentDao.update(mainComment);

        log.info("Updated comment: {}", mainComment);
        return modelMapper.map(mainComment, MainArticleCommentDto.class);
    }

    private ArticleComment getCommentForUpdate(ArticleCommentDto comment, int commentId) {
        log.info("Updating comment with id: {}", commentId);
        ArticleComment mainComment = articleCommentDao.getById(commentId);
        if (mainComment == null) {
            throw new EntityNotFoundException(exceptionMessage2 + commentId);
        }

        mainComment.setCreationDate(new Timestamp(System.currentTimeMillis()));
        mainComment.setContent(comment.getContent());
        return mainComment;
    }

    @Override
    public MainArticleCommentDto likeComment(int commentId) {
        log.info("Inc rating comment with id: {}", commentId);
        ArticleComment mainComment = articleCommentDao.getById(commentId);
        validateComment(mainComment, commentId);
        List<User> likers = getCommentLikers(mainComment);
        List<User> dislikers = getCommentDislikers(mainComment);
        User currentUser = modelMapper.map(userService.currentUser(), User.class);

        if (likers.contains(currentUser)) {
            removeEstimation(mainComment);
        } else if (dislikers.contains(currentUser)) {
            changeEstimation(mainComment);
        } else {
            addEstimation(mainComment, 1);
        }
        return modelMapper.map(mainComment, MainArticleCommentDto.class);
    }

    private void removeEstimation(ArticleComment mainComment) {
        User currentUser = modelMapper.map(userService.currentUser(), User.class);

        Optional<Estimation> estimation = mainComment.getEstimations().stream()
                .filter(cEstimation -> cEstimation
                        .getUser()
                        .equals(currentUser))
                .findFirst();

        if (!estimation.isPresent()) {
            throw new EntityNotFoundException(exceptionMessage5);
        }
        Set<Estimation> estimations = mainComment.getEstimations();

        estimations = estimations.stream()
                .filter(cEstimation -> !cEstimation
                        .equals(estimation.get()))
                .collect(Collectors.toSet());

        mainComment.setEstimations(estimations);
        estimationDao.delete(estimation.get());
    }

    @Override
    public MainArticleCommentDto dislikeComment(int commentId) {
        log.info("Dec rating comment with id: {}", commentId);
        ArticleComment mainComment = articleCommentDao.getById(commentId);
        User currentUser = modelMapper.map(userService.currentUser(), User.class);
        validateComment(mainComment, commentId);
        List<User> likers = getCommentLikers(mainComment);
        List<User> dislikers = getCommentDislikers(mainComment);

        if (dislikers.contains(currentUser)) {
            removeEstimation(mainComment);
        } else if (likers.contains(currentUser)) {
            changeEstimation(mainComment);
        } else {
            addEstimation(mainComment, -1);
        }
        return modelMapper.map(mainComment, MainArticleCommentDto.class);
    }

    private void changeEstimation(ArticleComment mainComment) {
        User currentUser = modelMapper.map(userService.currentUser(), User.class);

        Optional<Estimation> like = mainComment.getEstimations().stream()
                .filter(estimation -> estimation
                        .getUser()
                        .equals(currentUser))
                .findFirst();

        if (!like.isPresent()) {
            throw new EntityNotFoundException(exceptionMessage5);
        }
        int value = like.get().getValue();
        like.get().setValue(value * (-1));
        estimationDao.update(like.get());
    }

    private List<User> getCommentLikers(ArticleComment mainComment) {
        return mainComment.getEstimations().stream()
                .filter(estimation -> estimation.getValue() == 1)
                .map(Estimation::getUser)
                .collect(Collectors.toList());
    }

    private List<User> getCommentDislikers(ArticleComment mainComment) {
        return mainComment.getEstimations().stream()
                .filter(estimation -> estimation.getValue() == -1)
                .map(Estimation::getUser)
                .collect(Collectors.toList());
    }

    private boolean validateComment(ArticleComment mainComment, int commentId) {
        if (mainComment == null) {
            throw new EntityNotFoundException(exceptionMessage2 + commentId);
        }
        try {
            userService.currentUser();
        } catch (NullPointerException e) {
            throw new AccessDeniedException(exceptionMessage6);
        }
        if (mainComment.getUser().getId() == userService.currentUser().getId()) {
            throw new UserNotParticipantException(exceptionMessage4 + mainComment.getId());
        }
        return true;
    }

    private ArticleComment addEstimation(ArticleComment mainComment, int value) {
        Estimation estimation = new Estimation(0, value, modelMapper.map(userService.currentUser(), User.class), mainComment);
        mainComment.addEstimation(estimation);
        mainComment = articleCommentDao.update(mainComment);
        return mainComment;
    }

}
