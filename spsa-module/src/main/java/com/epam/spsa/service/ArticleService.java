package com.epam.spsa.service;

import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleService {

    MainArticleDto save(ArticleDto articleDto);

    void delete(int id);

    MainArticleDto update(ArticleDto articleDto, int id);

    MainArticleDto getById(int id);

    List<MainArticleDto> getAll();

    PaginationDto<MainArticleDto> getAllPaginated(int page, int size);

    MainArticleDto saveImageByArticleId(MultipartFile image, int articleId);

    MainArticleDto saveImageByArticleId(String url, int articleId);

    void deleteImageByArticleId(int articleId);

    MainArticleDto updateImageByArticleId(MultipartFile multipartFile, int articleId);

    MainArticleDto updateImageByArticleId(String url, int articleId);

}
