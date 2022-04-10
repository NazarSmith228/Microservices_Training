package com.epam.spsa.service;

import com.epam.spsa.controller.builder.ArticleCommentBuilder;
import com.epam.spsa.controller.builder.EstimationBuilder;
import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.dao.ArticleCommentDao;
import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.article.ArticleCommentDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.imageBuilder.FileBuilder;
import com.epam.spsa.model.Article;
import com.epam.spsa.model.ArticleComment;
import com.epam.spsa.model.Tag;
import com.epam.spsa.model.User;
import com.epam.spsa.s3api.S3Manager;
import com.epam.spsa.service.impl.ArticleCommentServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.*;

@ExtendWith(SpringExtension.class)
public class ArticleCommentServiceImplTest {

    @InjectMocks
    private ArticleCommentServiceImpl commentService;

    @Mock
    private ArticleCommentDao articleCommentDao;

    @Mock
    private ArticleDao articleDao;

    @Mock
    private UserService userService;

    @Mock
    private UserDao userDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private S3Manager s3Manager;

    private ArticleComment getComment(int id) {
        return ArticleComment.builder()
                .id(id)
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .content("))")
                .image("https:..")
                .build();
    }

    private MainArticleCommentDto getMainComment(int id) {
        return MainArticleCommentDto.builder()
                .id(id)
                .creationDate(new Timestamp(System.currentTimeMillis()))
                .content("))")
                .image("https:..")
                .build();
    }

    private Article getArticle(int id) {
        Set<Tag> tags = new HashSet<>();
        tags.add(new Tag());
        return Article.builder()
                .id(id)
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic")
                .description("description")
                .pictureUrl("http://localhost:8080")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .tags(tags)
                .build();
    }

    public void initMocks(int commentId, MockMultipartFile multipartFile) {
        if (commentId == 1) {
            Mockito.when(articleCommentDao.getById(commentId))
                    .thenAnswer((Answer<ArticleComment>) invocationOnMock -> {
                                ArticleComment com = ArticleCommentBuilder.getArticleComment(invocationOnMock.getArgument(0));
                                ArrayList<ArticleComment> list = new ArrayList<>();
                                com.setArticleComments(list);
                                com.setEstimations(new HashSet<>(Collections.singletonList(EstimationBuilder.getLike())));
                                return com;
                            }
                    );
            Mockito.when(modelMapper.map(Mockito.any(ArticleComment.class), Mockito.eq(MainArticleCommentDto.class)))
                    .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> {
                        ArticleComment comment1 = invocationOnMock.getArgument(0);
                        MainArticleCommentDto mainArticleCommentDto = getMainComment(comment1.getId());
                        mainArticleCommentDto.setContent(comment1.getContent());
                        mainArticleCommentDto.setLikes(1);
                        mainArticleCommentDto.setDislikes(1);
                        return mainArticleCommentDto;
                    });
        } else {
            Mockito.when(articleCommentDao.getById(commentId))
                    .thenAnswer((Answer<ArticleComment>) invocationOnMock -> {
                                ArticleComment com = ArticleCommentBuilder.getArticleComment(invocationOnMock.getArgument(0));
                                ArrayList<ArticleComment> list = new ArrayList<>();
                                com.setArticleComments(list);
                                com.setEstimations(new HashSet<>(Collections.singletonList(EstimationBuilder.getDislike())));
                                return com;
                            }
                    );
            Mockito.when(modelMapper.map(Mockito.any(ArticleComment.class), Mockito.eq(MainArticleCommentDto.class)))
                    .thenAnswer((Answer<MainArticleCommentDto>) invocationOnMock -> {
                        ArticleComment comment1 = invocationOnMock.getArgument(0);
                        MainArticleCommentDto mainArticleCommentDto = getMainComment(comment1.getId());
                        mainArticleCommentDto.setContent(comment1.getContent());
                        mainArticleCommentDto.setLikes(1);
                        mainArticleCommentDto.setDislikes(1);
                        return mainArticleCommentDto;
                    });
        }


        Mockito.when(userService.currentUser())
                .thenAnswer((Answer<MainUserDto>) invocationOnMock -> {
                    MainUserDto dto = UserBuilder.getMainUserDto();
                    dto.setId(7);
                    return dto;
                });

        Mockito.when(s3Manager.saveImageById(multipartFile, 1, "blogComments"))
                .thenReturn("https:...");

        Mockito.when(articleCommentDao.update(Mockito.any(ArticleComment.class)))
                .thenAnswer((Answer<ArticleComment>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(modelMapper.map(Mockito.any(ArticleCommentDto.class), Mockito.eq(ArticleComment.class)))
                .thenAnswer((Answer<ArticleComment>) invocationOnMock -> {
                    ArticleCommentDto commentDto = invocationOnMock.getArgument(0);
                    ArticleComment comment1 = getComment(0);
                    comment1.setContent(commentDto.getContent());
                    return comment1;
                });

        Mockito.when(articleCommentDao.save(Mockito.any(ArticleComment.class)))
                .then((Answer<ArticleComment>) invocationOnMock -> {
                    ArticleComment comment1 = invocationOnMock.getArgument(0);
                    comment1.setId(1);
                    return comment1;
                });
    }

    @Test
    public void getCommentTest() {
        int commentId = 1;
        initMocks(commentId, null);

        MainArticleCommentDto articleCommentDto = commentService.getComment(commentId);
        Assertions.assertEquals(commentId, articleCommentDto.getId());
    }

    @Test
    public void getCommentIncorrectTest() {
        int commentId = 1;

        Assertions.assertThrows(EntityNotFoundException.class, () -> commentService.getComment(commentId));
    }

    @Test
    public void getAllByArticleIdTest1() {
        int articleId = 1;
        int type = -5;

        Mockito.when(articleDao.getById(articleId))
                .thenAnswer((Answer<Article>) invocationOnMock -> getArticle(invocationOnMock.getArgument(0)));

        Mockito.when(articleCommentDao.getAllByArticleIdASC(articleId))
                .thenAnswer((Answer<List<ArticleComment>>) invocationOnMock -> {
                    List<ArticleComment> list = new ArrayList<>();
                    list.add(getComment(1));
                    list.add(getComment(2));
                    return list;
                });

        initMocks(0, null);
        List<MainArticleCommentDto> commentDtoList = commentService.getAllByArticleId(articleId, type);

        Assertions.assertEquals(2, commentDtoList.size());
    }

    @Test
    public void getAllByArticleIdTest2() {
        int articleId = 1;
        int type = 0;

        Mockito.when(articleDao.getById(articleId))
                .thenAnswer((Answer<Article>) invocationOnMock -> getArticle(invocationOnMock.getArgument(0)));

        Mockito.when(articleCommentDao.getAllByArticleId(articleId))
                .thenAnswer((Answer<List<ArticleComment>>) invocationOnMock -> {
                    List<ArticleComment> list = new ArrayList<>();
                    list.add(getComment(1));
                    list.add(getComment(2));
                    return list;
                });

        initMocks(0, null);
        List<MainArticleCommentDto> commentDtoList = commentService.getAllByArticleId(articleId, type);

        Assertions.assertEquals(2, commentDtoList.size());
    }

    @Test
    public void getAllByArticleIdTest3() {
        int articleId = 1;
        int type = 5;

        Mockito.when(articleDao.getById(articleId))
                .thenAnswer((Answer<Article>) invocationOnMock -> getArticle(invocationOnMock.getArgument(0)));

        Mockito.when(articleCommentDao.getAllByArticleIdDESK(articleId))
                .thenAnswer((Answer<List<ArticleComment>>) invocationOnMock -> {
                    List<ArticleComment> list = new ArrayList<>();
                    list.add(getComment(1));
                    list.add(getComment(2));
                    return list;
                });

        initMocks(0, null);
        List<MainArticleCommentDto> commentDtoList = commentService.getAllByArticleId(articleId, type);

        Assertions.assertEquals(2, commentDtoList.size());
    }

    @Test
    public void getAllByArticleIdIncorrectTest() {
        int articleId = 1;
        int type = 5;
        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.getAllByArticleId(articleId, type));
    }

    @Test
    public void deleteCommentTest() {
        int commentId = 1;
        initMocks(commentId, null);

        Assertions.assertDoesNotThrow(() -> commentService.deleteComment(commentId));
    }

    @Test
    public void deleteCommentIncorrectTest() {
        int id = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.deleteComment(id));
    }

    @Test
    public void getAllTest() {
        Mockito.when(articleCommentDao.getAll())
                .thenAnswer((Answer<List<ArticleComment>>) invocationOnMock -> {
                    List<ArticleComment> list = new ArrayList<>();
                    list.add(getComment(1));
                    list.add(getComment(2));
                    return list;
                });

        initMocks(0, null);
        List<MainArticleCommentDto> commentDtoList = commentService.getAll();

        Assertions.assertEquals(2, commentDtoList.size());
    }

    @Test
    public void saveCommentWithImageTest() throws IOException {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int userId = 1;
        int articleId = 1;

        Mockito.when(userDao.getById(userId)).thenAnswer((Answer<User>) invocationOnMock -> User.builder()
                .id(invocationOnMock.getArgument(0))
                .name("Max")
                .build());

        Mockito.when(articleDao.getById(articleId))
                .thenAnswer((Answer<Article>) invocationOnMock -> getArticle(invocationOnMock.getArgument(0)));

        initMocks(0, multipartFile);

        MainArticleCommentDto commentDto = commentService.saveCommentWithImage(multipartFile, comment, userId, articleId);

        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    public void saveCommentWithImageIncorrectTest() throws IOException {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int userId = 1;
        int articleId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.saveCommentWithImage(multipartFile, comment, userId, articleId));
    }

    @Test
    public void saveCommentWithImageIncorrectTest2() throws IOException {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int userId = 1;
        int articleId = 1;

        Mockito.when(userDao.getById(userId)).thenAnswer((Answer<User>) invocationOnMock -> User.builder()
                .id(invocationOnMock.getArgument(0))
                .name("Max")
                .build());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.saveCommentWithImage(multipartFile, comment, userId, articleId));
    }

    @Test
    public void saveCommentTest() {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("My content")
                .build();
        int userId = 1;
        int articleId = 1;

        Mockito.when(userDao.getById(userId)).thenAnswer((Answer<User>) invocationOnMock -> User.builder()
                .id(invocationOnMock.getArgument(0))
                .name("Max")
                .build());

        Mockito.when(articleDao.getById(articleId))
                .thenAnswer((Answer<Article>) invocationOnMock -> getArticle(invocationOnMock.getArgument(0)));

        initMocks(0, null);

        MainArticleCommentDto commentDto = commentService.saveComment(comment, userId, articleId);

        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    public void saveReplyCommentWithImageTest() throws IOException {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        int userId = 1;
        int commentId = 1;

        Mockito.when(userDao.getById(userId)).thenAnswer((Answer<User>) invocationOnMock -> User.builder()
                .id(invocationOnMock.getArgument(0))
                .name("Max")
                .build());

        initMocks(commentId, multipartFile);

        MainArticleCommentDto commentDto =
                commentService.saveReplyCommentWithImage(multipartFile, comment, userId, commentId);

        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    public void saveReplyCommentTest() throws IOException {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int userId = 1;
        int commentId = 1;

        Mockito.when(userDao.getById(userId)).thenAnswer((Answer<User>) invocationOnMock -> User.builder()
                .id(invocationOnMock.getArgument(0))
                .name("Max")
                .build());

        initMocks(commentId, null);

        MainArticleCommentDto commentDto =
                commentService.saveReplyComment(comment, userId, commentId);

        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    public void saveReplyCommentIncorrectTest1() {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int userId = 1;
        int commentId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.saveReplyComment(comment, userId, commentId));
    }

    @Test
    public void saveReplyCommentIncorrectTest2() {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int userId = 1;
        int commentId = 1;

        Mockito.when(userDao.getById(userId)).thenAnswer((Answer<User>) invocationOnMock -> User.builder()
                .id(invocationOnMock.getArgument(0))
                .name("Max")
                .build());

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.saveReplyComment(comment, userId, commentId));
    }

    @Test
    public void updateCommentWithImageTest() throws IOException {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        int commentId = 1;

        initMocks(commentId, multipartFile);

        MainArticleCommentDto commentDto = commentService.updateCommentWithImage(multipartFile, comment, commentId);

        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    public void updateCommentTest() {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int commentId = 1;

        initMocks(commentId, null);

        MainArticleCommentDto commentDto = commentService.updateComment(comment, commentId);

        Assertions.assertEquals(comment.getContent(), commentDto.getContent());
    }

    @Test
    public void updateCommentIncorrectTest1() {
        ArticleCommentDto comment = ArticleCommentDto.builder()
                .content("))))")
                .build();
        int commentId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.updateComment(comment, commentId));
    }

    @Test
    public void likeCommentTest() {
        int commentId = 1;

        initMocks(commentId, null);

        MainArticleCommentDto articleCommentDto = commentService.likeComment(commentId);
        Assertions.assertEquals(1, articleCommentDto.getLikes());
    }

    @Test
    public void dislikeCommentTest() {
        int commentId = 2;

        initMocks(commentId, null);

        MainArticleCommentDto articleCommentDto = commentService.dislikeComment(commentId);
        Assertions.assertEquals(1, articleCommentDto.getDislikes());
    }

    @Test
    public void updateRatingDecIncorrectTest() {
        int commentId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> commentService.likeComment(commentId));
    }

}
