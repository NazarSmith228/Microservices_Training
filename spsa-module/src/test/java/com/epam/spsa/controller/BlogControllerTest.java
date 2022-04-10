package com.epam.spsa.controller;

import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.dao.TagDao;
import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.imageBuilder.FileBuilder;
import com.epam.spsa.model.Tag;
import com.epam.spsa.service.ArticleService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BlogController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
class BlogControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ArticleService service;

    @MockBean
    private ArticleDao articleDao;

    @MockBean
    private TagDao tagDao;

    @MockBean
    private ModelMapper mapper;

    @Test
    void createArticleIncorrectTest() throws Exception {
        when(service.save(getArticleDto())).thenReturn(getMainArticleDto());
        mockMvc.perform(post("/api/v1/blogs/create"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createArticleIncorrectTagTest() throws Exception {
        when(service.save(getArticleDto())).thenReturn(getMainArticleDto());
        when(articleDao.getArticleByTopic("test topic")).thenThrow(new NoResultException());
        when(tagDao.getAll()).thenReturn(getTagList());

        mockMvc.perform(post("/api/v1/blogs/create")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content("{\"authorName\":\"Name\"," +
                        "\"authorSurname\":\"Surname\"," +
                        "\"topic\":\"test topic\"," +
                        "\"pictureUrl\":\"https://www.baeldung.com/hibernate-lob\"," +
                        "\"creationDate\":\"2020-03-17\"," +
                        "\"content\":\"content\"," +
                        "\"tags\": [{\"id\": 100," +
                        "\"name\":\"Swimming\"}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createArticleCorrectTagTest() throws Exception {
        when(articleDao.getArticleByTopic("test topic")).thenThrow(new NoResultException());
        when(tagDao.getAll()).thenReturn(getTagList());

        mockMvc.perform(post("/api/v1/blogs/create")
                .contentType(MediaType.APPLICATION_JSON)
                .characterEncoding("utf-8")
                .content("{\"authorName\":\"Name\"," +
                        "\"authorSurname\":\"Surname\"," +
                        "\"topic\":\"test topic\"," +
                        "\"pictureUrl\":\"https://www.baeldung.com/hibernate-lob\"," +
                        "\"creationDate\":\"2020-03-17\"," +
                        "\"content\":\"content\"," +
                        "\"tags\": [{\"id\": 100," +
                        "\"name\":\"Swimming\"}]}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateArticleCorrectTest() throws Exception {
        when(service.update(getArticleDto(), 1)).thenReturn(getMainArticleDto());
        when(articleDao.getArticleByTopic("test topic")).thenThrow(new NoResultException());


        mockMvc.perform(put("/api/v1/blogs/update/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"authorName\":\"Name\"," +
                        "\"authorSurname\":\"Surname\"," +
                        "\"topic\":\"test topic\"," +
                        "\"pictureUrl\":\"https://www.baeldung.com/hibernate-lob\"," +
                        "\"creationDate\":\"2020-03-17\"," +
                        "\"content\":\"content\"," +
                        "\"tags\":[]}")).andExpect(status().isOk());
    }

    @Test
    void getArticleByIdTest() throws Exception {
        when(service.getById(1)).thenReturn(getMainArticleDto());

        mockMvc.perform(get("/api/v1/blogs/articles/1"))
                .andExpect(content().string(
                        "{\"id\":1,\"authorName\":\"Name\"," +
                                "\"authorSurname\":\"Surname\"," +
                                "\"topic\":\"topic\",\"description\":\"description\"," +
                                "\"pictureUrl\":\"https://www.baeldung.com/hibernate-lob\"," +
                                "\"creationDate\":\"2020-03-17\"," +
                                "\"content\":\"content\"," +
                                "\"tags\":null}"))
                .andExpect(status().isOk());
    }

    @Test
    void getAllTest() throws Exception {
        when(service.getAllPaginated(1, 1))
                .thenReturn(getAllArticleDto());

        mockMvc.perform(get("/api/v1/blogs?page=1&size=1"))
                .andExpect(content().string("{\"entities\":[{\"id\":1,\"authorName\":\"Name\"," +
                        "\"authorSurname\":\"Surname\"," +
                        "\"topic\":\"topic\",\"description\":\"description\"," +
                        "\"pictureUrl\":\"https://www.baeldung.com/hibernate-lob\"," +
                        "\"creationDate\":\"2020-03-17\"," +
                        "\"content\":\"content\"," +
                        "\"tags\":null}]," +
                        "\"quantity\":1," +
                        "\"entitiesLeft\":0}"))
                .andExpect(status().isOk());
    }

    @Test
    public void saveImageByArticleIdTest() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(service.saveImageByArticleId(multipartFile, id))
                .thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
                    MainArticleDto mainArticleDto = getMainArticleDto();
                    mainArticleDto.setPictureUrl("http...");
                    return mainArticleDto;
                });

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/blogs/{id}/image", id)
                .file(multipartFile))
                .andExpect(jsonPath("$.pictureUrl").value("http..."))
                .andExpect(status().isCreated());
    }

    @Test
    public void saveImageByArticleIdIncorrectTest() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "txtTest.txt", "txt/plane", FileBuilder.getTxtBytes());

        Mockito.when(service.saveImageByArticleId(multipartFile, id))
                .thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
                    MainArticleDto mainArticleDto = getMainArticleDto();
                    mainArticleDto.setPictureUrl("http...");
                    return mainArticleDto;
                });

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/blogs/{id}/image", id)
                .file(multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void saveUrlImageByArticleIdTest() throws Exception {
        int id = 1;
        String url = "https:...";

        Mockito.when(service.saveImageByArticleId(url, id))
                .thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
                    MainArticleDto mainArticleDto = getMainArticleDto();
                    mainArticleDto.setPictureUrl(invocationOnMock.getArgument(0));
                    return mainArticleDto;
                });

        mockMvc.perform(post("/api/v1/blogs/{id}/image-url", id)
                .param("url", url))
                .andExpect(jsonPath("$.pictureUrl").value(url))
                .andExpect(status().isCreated());
    }

    @Test
    public void deleteImageByArticleIdTest() throws Exception {
        int id = 1;

        Mockito.doNothing().when(service).deleteImageByArticleId(id);

        mockMvc.perform(delete("/api/v1/blogs/{id}/image", id))
                .andExpect(status().isOk());
    }

    @Test
    public void updateImageByArticleId() throws Exception {
        int id = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(service.updateImageByArticleId(multipartFile, id))
                .thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
                    MainArticleDto mainArticleDto = getMainArticleDto();
                    mainArticleDto.setPictureUrl("http...");
                    return mainArticleDto;
                });

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/blogs/{id}/image", id);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(multipartFile))
                .andExpect(jsonPath("$.pictureUrl").value("http..."))
                .andExpect(status().isOk());
    }

    @Test
    public void updateUrlImageByArticleId() throws Exception {
        int id = 1;
        String url = "https:...";

        Mockito.when(service.updateImageByArticleId(url, id))
                .thenAnswer((Answer<MainArticleDto>) invocationOnMock -> {
                    MainArticleDto mainArticleDto = getMainArticleDto();
                    mainArticleDto.setPictureUrl(invocationOnMock.getArgument(0));
                    return mainArticleDto;
                });

        mockMvc.perform(put("/api/v1/blogs/{id}/image-url", id)
                .param("url", url))
                .andExpect(jsonPath("$.pictureUrl").value(url))
                .andExpect(status().isOk());
    }

    private ArticleDto getArticleDto() {
        return ArticleDto.builder()
                .authorName("Name")
                .authorSurname("Surname")
                .topic("topic")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .tags(new HashSet<>())
                .build();
    }

    private Set<Tag> getTag() {
        Set<Tag> tags = new HashSet<>();
        Tag tag = Tag.builder().id(1).name("Yoga").build();
        tags.add(tag);
        return tags;
    }

    private List<Tag> getTagList() {
        return new ArrayList<>(getTag());
    }

    private MainArticleDto getMainArticleDto() {

        return MainArticleDto.builder()
                .id(1)
                .authorName("Name")
                .authorSurname("Surname")
                .topic("topic")
                .description("description")
                .pictureUrl("https://www.baeldung.com/hibernate-lob")
                .creationDate(LocalDate.parse("2020-03-17"))
                .content("content")
                .tags(null)
                .build();
    }

    private PaginationDto<MainArticleDto> getAllArticleDto() {
        return PaginationDto.<MainArticleDto>builder()
                .entities(Arrays.asList(getMainArticleDto()))
                .quantity(1)
                .entitiesLeft(0)
                .build();
    }

}
