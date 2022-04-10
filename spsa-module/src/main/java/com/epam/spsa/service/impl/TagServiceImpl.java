package com.epam.spsa.service.impl;

import com.epam.spsa.dao.TagDao;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.dto.tag.TagDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.model.Tag;
import com.epam.spsa.service.TagService;
import com.epam.spsa.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:/messages.properties")
@Slf4j
public class TagServiceImpl implements TagService {

    private final TagDao tagDao;

    private final ModelMapper mapper;

    @Value("${tag.exception.id.notfound}")
    private String exceptionMessageId;

    @Value("${tag.exception.name.notfound}")
    private String exceptionMessageName;

    @Value("${pagination.exception}")
    private String exceptionPagination;

    @Override
    public MainTagDto save(TagDto tagDto) {
        log.info("Saving TagDto: {}", tagDto);
        tagDto.setName(tagDto.getName().toUpperCase());
        Tag tag = mapper.map(tagDto, Tag.class);

        tagDao.save(tag);

        return mapper.map(tag, MainTagDto.class);
    }

    @Override
    public void delete(int id) {
        log.info("Deleting Tag by id: {}", id);
        Tag tag = getTagById(id);
        log.debug("Tag to delete: {}", tag);
        tagDao.delete(tag);
    }

    @Override
    public MainTagDto getById(int id) {
        log.debug("Getting Tag  by id: {}", id);
        return mapper.map(getTagById(id), MainTagDto.class);
    }

    @Override
    public List<MainTagDto> getAll() {
        log.info("Getting list of MainTagDto");
        return tagDao
                .getAll()
                .stream()
                .map(tag -> mapper.map(tag, MainTagDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaginationDto<MainArticleDto> getAllArticlesByTagName(String name, int pageNumber, int pageSize) {
        log.info("Getting list of Articles by tag name: {}", name);

        Tag tag = getTagByName(name);
        List<MainArticleDto> articleDtoList = tag.getArticles().stream()
                .map(article -> mapper.map(article, MainArticleDto.class))
                .collect(Collectors.toList());
        return PaginationUtils.paginate(articleDtoList, pageNumber, pageSize);
    }

    private Tag getTagById(int id) {
        log.info("Getting Tag by id: {}", id);
        Tag tag = tagDao.getById(id);
        if (tag == null) {
            log.error("Tag wasn't found with id: {}", id);
            throw new EntityNotFoundException(exceptionMessageId + id);
        }
        log.info("Found Tag: {}", tag);
        return tag;
    }

    private Tag getTagByName(String name) {
        log.info("Getting Tag by name: {}", name);
        Tag tag;
        try {
            tag = tagDao.getByName(name.toUpperCase());
        } catch (Exception e) {
            log.error("Tag wasn't found with name: {}", name);
            throw new EntityNotFoundException(exceptionMessageName + name);
        }
        log.info("Found Tag: {}", tag);
        return tag;
    }

}
