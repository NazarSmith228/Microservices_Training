package com.epam.spsa.service;

import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.tag.MainTagDto;
import com.epam.spsa.dto.tag.TagDto;

import java.util.List;

public interface TagService {

    MainTagDto save(TagDto tagDto);

    void delete(int id);

    MainTagDto getById(int id);

    List<MainTagDto> getAll();

    PaginationDto<MainArticleDto> getAllArticlesByTagName(String name, int pageNumber, int pageSize);

}
