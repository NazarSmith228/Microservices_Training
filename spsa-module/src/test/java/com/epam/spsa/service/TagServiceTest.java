package com.epam.spsa.service;

import com.epam.spsa.dao.TagDao;
import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.dto.tag.TagDto;
import com.epam.spsa.model.Article;
import com.epam.spsa.model.Tag;
import com.epam.spsa.service.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TagServiceTest {

    @Mock
    private TagDao tagDao;

    @InjectMocks
    private TagServiceImpl tagService;

    @Mock
    private ModelMapper mapper;

    @Test
    public void saveTest() {
        TagDto tagDto = getTagDto();
        Tag tag = getTag();
        MainTagDto mainTagDto = getMainTagDto();

        when(tagDao.save(any(Tag.class))).thenReturn(tag);
        when(mapper.map(tag, MainTagDto.class)).thenReturn(mainTagDto);
        when(mapper.map(tagDto, Tag.class)).thenReturn(tag);

        MainTagDto newTagDto = tagService.save(tagDto);

        assertEquals(mainTagDto, newTagDto);
    }

    @Test
    public void getAll() {

        Tag tag1 = Tag.builder().id(1).name("Yoga").build();
        Tag tag2 = Tag.builder().id(2).name("Swimming").build();

        MainTagDto mainTagDto1 = MainTagDto.builder().id(1).name("Yoga").build();
        MainTagDto mainTagDto2 = MainTagDto.builder().id(2).name("Swimming").build();

        List<Tag> tags = new ArrayList<>(Arrays.asList(tag1, tag2));

        when(tagDao.getAll()).thenReturn(tags);
        when(mapper.map(tag1, MainTagDto.class)).thenReturn(mainTagDto1);
        when(mapper.map(tag2, MainTagDto.class)).thenReturn(mainTagDto2);

        List<MainTagDto> mainTagDtos = tagService.getAll();

        assertEquals(mainTagDtos.size(), tags.size());
    }

    @Test
    public void getById() {
        Tag tag = getTag();
        MainTagDto tagDto = getMainTagDto();

        when(tagDao.getById(tag.getId())).thenReturn(tag);
        when(mapper.map(tag, MainTagDto.class)).thenReturn(tagDto);

        assertEquals("MainTagDto(id=10, name=Yoga)", tagService.getById(tag.getId()).toString());
    }

    @Test
    public void getAllArticlesByTagNameWithValidPaginationTest() {
        int pageNumber = 1;
        int pageSize = 3;
        Tag tag = getTag();

        tag.setArticles(new HashSet<>(getArticles()));

        when(tagDao.getByName(tag.getName().toUpperCase())).thenReturn(tag);
        when(mapper.map(getArticles().get(0), ArticleDto.class)).thenReturn(getArticleDtos().get(0));
        when(mapper.map(getArticles().get(1), ArticleDto.class)).thenReturn(getArticleDtos().get(1));

        PaginationDto paginationDto = tagService.getAllArticlesByTagName(tag.getName(), pageNumber, pageSize);

        assertEquals(getArticles().size(), paginationDto.getEntities().size());
    }


    public List<Article> getArticles() {
        Article article1 = Article.builder()
                .authorName("Yura")
                .authorSurname("Khanas")
                .content("Lorem")
                .description("Lorem")
                .creationDate(LocalDate.from(LocalDateTime.now()))
                .pictureUrl("photo")
                .topic("Yoga")
                .tags(new HashSet<>(Collections.singletonList(getTag())))
                .build();
        Article article2 = Article.builder()
                .authorName("Ivan")
                .authorSurname("Petrov")
                .content("Lorem")
                .description("Lorem")
                .creationDate(LocalDate.from(LocalDateTime.now()))
                .pictureUrl("photo")
                .topic("Yoga")
                .tags(new HashSet<>(Collections.singletonList(getTag())))
                .build();

        return new ArrayList<>(Arrays.asList(article1, article2));
    }

    public List<ArticleDto> getArticleDtos() {
        ArticleDto articleDto1 = ArticleDto.builder()
                .authorName("Yura")
                .authorSurname("Khanas")
                .content("Lorem")
                .creationDate(LocalDate.from(LocalDateTime.now()))
                .topic("Yoga")
                .build();
        ArticleDto articleDto2 = ArticleDto.builder()
                .authorName("Ivan")
                .authorSurname("Petrov")
                .content("Lorem")
                .creationDate(LocalDate.from(LocalDateTime.now()))
                .topic("Yoga")
                .build();

        return new ArrayList<>(Arrays.asList(articleDto1, articleDto2));
    }

    public Tag getTag() {
        int tagId = 10;
        String tagName = "Yoga";
        return Tag.builder().id(tagId).name(tagName).build();
    }

    public MainTagDto getMainTagDto() {
        int tagId = 10;
        String tagName = "Yoga";
        return MainTagDto.builder().id(tagId).name(tagName).build();
    }

    public TagDto getTagDto() {
        int tagId = 10;
        String tagName = "Yoga";
        return TagDto.builder().name(tagName).build();
    }

}
