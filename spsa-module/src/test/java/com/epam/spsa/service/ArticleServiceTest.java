package com.epam.spsa.service;

import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.PaginationException;
import com.epam.spsa.error.exception.PhotoException;
import com.epam.spsa.imageBuilder.FileBuilder;
import com.epam.spsa.model.Article;
import com.epam.spsa.model.Tag;
import com.epam.spsa.s3api.S3Manager;
import com.epam.spsa.service.impl.ArticleServiceImpl;
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
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class ArticleServiceTest {

    @InjectMocks
    private ArticleServiceImpl service;

    @Mock
    private ArticleDao dao;

    @Mock
    private ModelMapper mapper;

    @Mock
    private S3Manager manager;

    @Test
    void saveTest() {
        Article article = getArticle();
        ArticleDto articleDto = getArticleDto();
        MainArticleDto mainArticleDto = getMainArticleDto();

        when(dao.save(Mockito.any(Article.class))).thenReturn(article);
        when(mapper.map(Mockito.any(Article.class), Mockito.eq(ArticleDto.class))).thenReturn(articleDto);
        when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenReturn(mainArticleDto);
        when(mapper.map(Mockito.any(ArticleDto.class), Mockito.eq(Article.class))).thenReturn(article);

        MainArticleDto result = service.save(articleDto);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mainArticleDto, result);
    }

    @Test
    void updateTest() {
        Article article = getArticle();
        ArticleDto articleDto = getArticleDto();
        MainArticleDto mainArticleDto = getMainArticleDto();

        when(mapper.map(Mockito.any(Article.class), Mockito.eq(ArticleDto.class))).thenReturn(articleDto);
        when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenReturn(mainArticleDto);
        when(mapper.map(Mockito.any(ArticleDto.class), Mockito.eq(Article.class))).thenReturn(article);
        when(dao.getById(1)).thenReturn(article);
        when(dao.update(Mockito.any(Article.class))).thenReturn(article);

        MainArticleDto result = service.update(articleDto, 1);
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mainArticleDto, result);
    }

    @Test
    void getByIdTest() {
        Article article = getArticle();
        ArticleDto articleDto = getArticleDto();
        MainArticleDto mainArticleDto = getMainArticleDto();

        when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenReturn(mainArticleDto);
        when(dao.getById(1)).thenReturn(article);

        MainArticleDto result = service.getById(1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(mainArticleDto, result);
    }

    @Test
    void getAllTest() {
        List<Article> list = getListArticle(5);

        when(dao.getAll()).thenReturn(list);
        when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class)))
                .thenAnswer(
                        (Answer<MainArticleDto>) invocation -> {
                            Article article = invocation.getArgument(0);
                            int id = article.getId();
                            return getMainArticleDto(id);
                        }
                );

        List<MainArticleDto> result = service.getAll();

        Assertions.assertNotNull(result);
        Assertions.assertEquals("topic5", result.get(4).getTopic());
        Assertions.assertEquals(5, result.size());
    }

    @Test
    void getAllPaginatedTest() {
        List<Article> list = getListArticle(5);

        when(dao.getAll()).thenReturn(list);

        when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class)))
                .thenAnswer(
                        (Answer<MainArticleDto>) invocation -> {
                            Article article = invocation.getArgument(0);
                            int id = article.getId();
                            return getMainArticleDto(id);
                        }
                );

        PaginationDto<MainArticleDto> result = service.getAllPaginated(3, 1);

        Assertions.assertNotNull(result);
        Assertions.assertEquals(1, result.getEntities().size());
        Assertions.assertEquals("topic3", result.getEntities().get(0).getTopic());
    }

    @Test
    void getAllPaginatedOutOfPageTest() {
        List<Article> list = getListArticle(5);

        when(dao.getAll()).thenReturn(list);
        when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class)))
                .thenAnswer(
                        (Answer<MainArticleDto>) invocation -> {
                            Article article = invocation.getArgument(0);
                            int id = article.getId();
                            return getMainArticleDto(id);
                        }
                );
        Assertions.assertThrows(PaginationException.class,
                () -> service.getAllPaginated(3, 5));
    }

    @Test
    public void saveImageByArticleIdTest() throws IOException {
        int articleId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> {
            Article article = getArticle();
            article.setPictureUrl(null);
            return article;
        });

        Mockito.when(manager.saveImageById(multipartFile, articleId, "blog"))
                .thenReturn("https:...");

        Mockito.when(dao.update(Mockito.any(Article.class)))
                .then((Answer<Article>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
            MainArticleDto mainArticleDto = getMainArticleDto();
            Article article = invocationOnMock.getArgument(0);
            mainArticleDto.setPictureUrl(article.getPictureUrl());
            return mainArticleDto;
        });

        MainArticleDto mainArticleDto = service.saveImageByArticleId(multipartFile, articleId);
        Assertions.assertEquals("https:...", mainArticleDto.getPictureUrl());
    }

    @Test
    public void saveImageByArticleIdIncorrectTest() throws IOException {
        int articleId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.saveImageByArticleId(multipartFile, articleId));
    }

    @Test
    public void saveImageByArticleIdTest2() throws IOException {
        int articleId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> getArticle());

        Assertions.assertThrows(PhotoException.class, () -> service.saveImageByArticleId(multipartFile, articleId));
    }

    @Test
    public void saveUrlImageByArticleIdTest() {
        int articleId = 1;
        String url = "https:...";

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> {
            Article article = getArticle();
            article.setPictureUrl(null);
            return article;
        });

        Mockito.when(dao.update(Mockito.any(Article.class)))
                .then((Answer<Article>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
            MainArticleDto mainArticleDto = getMainArticleDto();
            Article article = invocationOnMock.getArgument(0);
            mainArticleDto.setPictureUrl(article.getPictureUrl());
            return mainArticleDto;
        });

        MainArticleDto mainArticleDto = service.saveImageByArticleId(url, articleId);
        Assertions.assertEquals(url, mainArticleDto.getPictureUrl());
    }

    @Test
    public void saveUrlImageByArticleIdIncorrectTest() {
        int articleId = 1;
        String url = "https:...";

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.saveImageByArticleId(url, articleId));
    }

    @Test
    public void saveUrlImageByArticleIdIncorrectTest2() {
        int articleId = 1;
        String url = "https:...";

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> getArticle());

        Assertions.assertThrows(PhotoException.class, () -> service.saveImageByArticleId(url, articleId));
    }

    @Test
    public void deleteImageByArticleIdTest() {
        int articleId = 1;

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> {
            Article article = getArticle();
            article.setPictureUrl(null);
            return article;
        });

        Mockito.doNothing().when(manager).deleteFileByUserId(articleId, "blog");

        Mockito.when(dao.update(Mockito.any(Article.class)))
                .then((Answer<Article>) invocationOnMock -> invocationOnMock.getArgument(0));

        Assertions.assertDoesNotThrow(() -> service.deleteImageByArticleId(articleId));
    }

    @Test
    public void deleteImageByArticleIdIncorrectTest() {
        int articleId = 1;

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.deleteImageByArticleId(articleId));
    }

    @Test
    public void updateImageByArticleIdTest() throws IOException {
        int articleId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(dao.getById(articleId)).thenReturn(getArticle());

        Mockito.when(manager.saveImageById(multipartFile, articleId, "blog"))
                .thenReturn("https:...");

        Mockito.when(dao.update(Mockito.any(Article.class)))
                .then((Answer<Article>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
            MainArticleDto mainArticleDto = getMainArticleDto();
            Article article = invocationOnMock.getArgument(0);
            mainArticleDto.setPictureUrl(article.getPictureUrl());
            return mainArticleDto;
        });

        MainArticleDto mainArticleDto = service.updateImageByArticleId(multipartFile, articleId);
        Assertions.assertEquals("https:...", mainArticleDto.getPictureUrl());
    }

    @Test
    public void updateImageByArticleIdIncorrectTest() throws IOException {
        int articleId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.updateImageByArticleId(multipartFile, articleId));
    }

    @Test
    public void updateImageByArticleIdIncorrectTest2() throws IOException {
        int articleId = 1;

        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> {
            Article article = getArticle();
            article.setPictureUrl(null);
            return article;
        });

        Assertions.assertThrows(PhotoException.class, () -> service.updateImageByArticleId(multipartFile, articleId));
    }

    @Test
    public void updateUrlImageByArticleIdTest() {
        int articleId = 1;
        String url = "https:...";

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> getArticle());

        Mockito.when(dao.update(Mockito.any(Article.class)))
                .then((Answer<Article>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(Article.class), Mockito.eq(MainArticleDto.class))).thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
            MainArticleDto mainArticleDto = getMainArticleDto();
            Article article = invocationOnMock.getArgument(0);
            mainArticleDto.setPictureUrl(article.getPictureUrl());
            return mainArticleDto;
        });

        MainArticleDto mainArticleDto = service.updateImageByArticleId(url, articleId);
        Assertions.assertEquals(url, mainArticleDto.getPictureUrl());
    }

    @Test
    public void updateUrlImageByArticleIdIncorrectTest() {
        int articleId = 1;
        String url = "https:...";

        Assertions.assertThrows(EntityNotFoundException.class, () -> service.updateImageByArticleId(url, articleId));
    }

    @Test
    public void updateUrlImageByArticleIdIncorrectTest2() {
        int articleId = 1;
        String url = "https:...";

        Mockito.when(dao.getById(articleId)).thenAnswer((Answer<Article>) invocationOnMock -> {
            Article article = getArticle();
            article.setPictureUrl(null);
            return article;
        });

        Assertions.assertThrows(PhotoException.class, () -> service.updateImageByArticleId(url, articleId));
    }

    private ArticleDto getArticleDto() {
        return ArticleDto.builder()
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic")
                .creationDate(LocalDate.parse("2020-03-17"))
                .tags(new HashSet<>())
                .content("content")
                .build();
    }

    private ArticleDto getArticleDto(int number) {
        return ArticleDto.builder()
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic" + number)
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .build();
    }

    private Article getArticle() {
        Set<Tag> tags = new HashSet<>();
        tags.add(getTag());
        return Article.builder()
                .id(1)
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

    private Article getArticle(int id) {
        Set<Tag> tags = new HashSet<>();
        tags.add(getTag());
        return Article.builder()
                .id(id)
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic" + id)
                .description("description")
                .pictureUrl("http://localhost:8080")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .tags(tags)
                .build();
    }

    private List<Article> getListArticle(int qty) {
        return Stream.iterate(1, i -> ++i)
                .limit(qty)
                .map(i -> getArticle(i))
                .collect(Collectors.toList());
    }

    private MainArticleDto getMainArticleDto() {
        return MainArticleDto.builder()
                .id(1)
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic")
                .description("description")
                .pictureUrl("http://localhost:8080")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .build();
    }

    private MainArticleDto getMainArticleDto(int id) {
        return MainArticleDto.builder()
                .id(id)
                .authorName("Name")
                .authorSurname("Secondname")
                .topic("topic" + id)
                .description("description")
                .pictureUrl("http://localhost:8080")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .build();
    }

    private Tag getTag() {
        return Tag.builder()
                .name("Yoga")
                .build();
    }
}