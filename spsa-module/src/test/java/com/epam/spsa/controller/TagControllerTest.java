package com.epam.spsa.controller;

import com.epam.spsa.dao.TagDao;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.dto.tag.TagDto;
import com.epam.spsa.service.TagService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = TagController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("default")
public class TagControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TagDao tagDao;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TagService tagService;

    @Test
    public void saveTagTest() throws Exception {
        String name = "Yoga";
        TagDto tagDto = TagDto.builder().name("Yoga").build();
        MainTagDto mainTagDto = MainTagDto.builder().id(2).name("Yoga").build();

        when(tagDao.getAll()).thenReturn(new ArrayList<>());
        when(tagService.save(tagDto)).thenReturn(mainTagDto);

        mockMvc.perform(post("/api/v1/tags/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void saveInvalidTagTest() throws Exception {
        String name = "Yoga";
        MainTagDto mainTagDto = MainTagDto.builder().name("Yoga").build();
        TagDto tagDto = TagDto.builder().name("Yoga").build();

        when(tagDao.getAll()).thenReturn(new ArrayList<>());
        when(tagService.save(tagDto)).thenReturn(mainTagDto);

        mockMvc.perform(post("/api/v1/tags/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tagDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void getTagByIdTest() throws Exception {
        int tagId = 1;
        String name = "Yoga";
        MainTagDto tagDto = MainTagDto.builder().id(tagId).name(name).build();

        when(tagService.getById(anyInt())).thenReturn(tagDto);

        mockMvc.perform(get("/api/v1/tags/{id}", 1)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(tagId))
                .andExpect(jsonPath("$.name").value(name));
    }

    @Test
    public void getAllTagsTest() throws Exception {
        int tagId1 = 1;
        String name1 = "Yoga";
        int tagId2 = 2;
        String name2 = "Swimming";
        MainTagDto tagDto1 = MainTagDto.builder().id(tagId1).name(name1).build();
        MainTagDto tagDto2 = MainTagDto.builder().id(tagId2).name(name2).build();
        List<MainTagDto> mainTagDtoList = new ArrayList<>(Arrays.asList(tagDto1, tagDto2));

        when(tagService.getAll()).thenReturn(mainTagDtoList);

        mockMvc.perform(get("/api/v1/tags/")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteTagTest() throws Exception {
        int id = 3;

        doNothing()
                .when(tagService)
                .delete(id);

        mockMvc.perform(delete("/api/v1/tags/{id}", id))
                .andExpect(status().isOk());
    }

    @Test
    public void getAllArticlesByTagNameTest() throws Exception {
        String pageNumber = "1";
        String pageSize = "3";
        int id = 1;
        String name = "Yoga";
        MainTagDto mainTagDto = MainTagDto.builder().id(id).name(name).build();
        MainArticleDto articleDto;
        articleDto = MainArticleDto.builder()
                .authorName("Yura")
                .authorSurname("Khanas")
                .content("Lorem")
                .creationDate(LocalDate.from(LocalDateTime.now()))
                .topic("Yoga")
                .tags(new HashSet<>(Collections.singletonList(mainTagDto)))
                .build();

        PaginationDto paginationDto = PaginationDto.builder()
                .entities(new ArrayList<>(Collections.singletonList(articleDto)))
                .entitiesLeft(0)
                .quantity(1)
                .build();


        when(tagService.getAllArticlesByTagName(name, Integer.parseInt(pageNumber), Integer.parseInt(pageSize)))
                .thenReturn(paginationDto);

        mockMvc.perform(get("/api/v1/tags/articles")
                .param("tag", name)
                .param("pageNumber", pageNumber)
                .param("pageSize", pageSize)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

}
