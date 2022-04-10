
package com.epam.spsa.service.impl;

import com.epam.spsa.dao.ArticleDao;
import com.epam.spsa.dao.TagDao;
import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.NotUniqueValueException;
import com.epam.spsa.error.exception.PhotoException;
import com.epam.spsa.model.Article;
import com.epam.spsa.model.Tag;
import com.epam.spsa.s3api.S3Manager;
import com.epam.spsa.service.ArticleService;
import com.epam.spsa.utils.PaginationUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("Duplicates")
public class ArticleServiceImpl implements ArticleService {

    private final ArticleDao articleDao;

    private final TagDao tagDao;

    private final ModelMapper mapper;

    private final S3Manager manager;

    @Value("${article.exception.notfound}")
    private String exceptionMessage;

    @Value("${article.photo.exception.alreadyExists}")
    private String exceptionMessage2;

    @Value("${article.photo.exception.notExists}")
    private String exceptionMessage3;

    @Override
    public MainArticleDto save(ArticleDto articleDto) {
        log.info("Saving ArticleDto: {}", articleDto);
        Article newArticle = mapper.map(articleDto, Article.class);
        newArticle.setTags(articleDto.getTags().stream()
                .map(mainTagDto -> mapper.map(mainTagDto, Tag.class))
                .collect(Collectors.toSet()));
        String description = createDescription(articleDto.getContent());
        newArticle.setDescription(description);
        articleDao.save(newArticle);
        articleDto.getTags().forEach(tag -> tagDao.getById(tag.getId()).getArticles().add(newArticle));
        return mapper.map(newArticle, MainArticleDto.class);
    }

    @Override
    public void delete(int id) {
        log.info("Deleting ArticleDto by id: {}", id);
        Article articleToDelete = getArticleById(id);
        articleToDelete.getTags().forEach(tag -> tagDao.getById(tag.getId()).getArticles().remove(articleToDelete));
        articleDao.delete(articleToDelete);
    }

    @Override
    public MainArticleDto update(ArticleDto editedArticleDto, int id) {
        log.info("Updating ArticleDto by id: {}", id);
        log.debug("Edited ArticleDto: {}", editedArticleDto);

        Article oldArticle = getArticleById(id);
        log.debug("Old Article: {}", oldArticle);

        Article test = null;
        try {
            test = articleDao.getArticleByTopic(editedArticleDto.getTopic());
        } catch (Exception ex) {
            log.warn(ex.getMessage());
        }
        if (test != null) {
            if (test.getId() != oldArticle.getId()) {
                log.error("Attempt to update Article with existing topic: {}", oldArticle.getTopic());
                throw new NotUniqueValueException("Topic already exists with another article!");
            }
        }
        mapper.map(editedArticleDto, oldArticle);

        oldArticle.setTags(editedArticleDto.getTags()
                .stream()
                .map(mainTagDto -> mapper.map(mainTagDto, Tag.class))
                .collect(Collectors.toSet()));

        return mapper.map(articleDao.update(oldArticle), MainArticleDto.class);
    }

    @Override
    public MainArticleDto getById(int id) {
        log.info("Getting ArticleDto by id: {}", id);
        Article article = getArticleById(id);
        return mapper.map(article, MainArticleDto.class);
    }

    @Override
    public List<MainArticleDto> getAll() {
        log.info("Getting list of ArticleDto");
        List<Article> articles = articleDao.getAll();
        log.debug("list of ArticleDto size: {}", articles.size());
        return articles.stream()
                .map(a -> mapper.map(a, MainArticleDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public PaginationDto<MainArticleDto> getAllPaginated(int page, int size) {
        List<Article> articles = articleDao.getAll();
        PaginationDto<Article> paginate = PaginationUtils.paginate(articles, page, size);

        articles = paginate.getEntities();
        articles.forEach(article -> article.setContent(""));
        List<MainArticleDto> resultList = articles.stream()
                .map(a -> mapper.map(a, MainArticleDto.class))
                .collect(Collectors.toList());

        return PaginationDto.<MainArticleDto>builder()
                .entities(resultList)
                .quantity(paginate.getQuantity())
                .entitiesLeft(paginate.getEntitiesLeft())
                .build();
    }

    private Article getArticleById(int id) {
        Article article = articleDao.getById(id);
        if (article == null) {
            throw new EntityNotFoundException("Article wasn't found: id=" + id);
        }
        return article;
    }

    private String createDescription(String content) {
        log.info("Creating description for ArticleDto");
        int srcEnd = 250;
        if (content.length() < srcEnd) {
            srcEnd = content.length();
        }
        String result = String.copyValueOf(content.toCharArray(), 0, srcEnd);
        log.info("Created description: {}", result);
        return result;
    }

    @Override
    public MainArticleDto saveImageByArticleId(MultipartFile image, int articleId) {
        log.info("Getting Article by id: {}", articleId);
        Article article = articleDao.getById(articleId);

        if (article == null) {
            log.error("Article wasn't found. id: {}", articleId);
            throw new EntityNotFoundException(exceptionMessage + articleId);
        } else if (article.getPictureUrl() != null) {
            log.error("The photo is already tied to the article with id: {}", articleId);
            throw new PhotoException(exceptionMessage2 + articleId);
        }

        log.info("Saving photo to S3");
        String imageUrl = manager.saveImageById(image, articleId, "blog");
        log.info("Updating article with id: {}", articleId);
        article.setPictureUrl(imageUrl);
        articleDao.update(article);
        return mapper.map(article, MainArticleDto.class);
    }

    @Override
    public MainArticleDto saveImageByArticleId(String url, int articleId) {
        log.info("Getting Article by id: {}", articleId);
        Article article = articleDao.getById(articleId);

        if (article == null) {
            log.error("Article wasn't found. id: {}", articleId);
            throw new EntityNotFoundException(exceptionMessage + articleId);
        } else if (article.getPictureUrl() != null) {
            log.error("The photo is already tied to the article with id: {}", articleId);
            throw new PhotoException(exceptionMessage2 + articleId);
        }

        log.info("Updating article with id: {}", articleId);
        article.setPictureUrl(url);
        articleDao.update(article);
        return mapper.map(article, MainArticleDto.class);
    }

    @Override
    public void deleteImageByArticleId(int articleId) {
        log.info("Getting Article by id: {}", articleId);
        Article article = articleDao.getById(articleId);

        if (article == null) {
            log.error("Article wasn't found. id: {}", articleId);
            throw new EntityNotFoundException(exceptionMessage + articleId);
        }

        manager.deleteFileByUserId(articleId, "blog");
        log.info("Updating article with id: {}", articleId);
        article.setPictureUrl(null);
        articleDao.update(article);
    }

    @Override
    public MainArticleDto updateImageByArticleId(MultipartFile multipartFile, int articleId) {
        log.info("Getting Article by id: {}", articleId);
        Article article = articleDao.getById(articleId);

        if (article == null) {
            log.error("Article wasn't found. id: {}", articleId);
            throw new EntityNotFoundException(exceptionMessage + articleId);
        } else if (article.getPictureUrl() == null) {
            log.error("The photo isn't tied to the article with id: {}", articleId);
            throw new PhotoException(exceptionMessage3 + articleId);
        }

        String newUrl = manager.saveImageById(multipartFile, articleId, "blog");
        log.info("Updating article with id: {}", articleId);
        article.setPictureUrl(newUrl);
        articleDao.update(article);
        return mapper.map(article, MainArticleDto.class);
    }

    @Override
    public MainArticleDto updateImageByArticleId(String url, int articleId) {
        log.info("Getting Article by id: {}", articleId);
        Article article = articleDao.getById(articleId);

        if (article == null) {
            log.error("Article wasn't found. id: {}", articleId);
            throw new EntityNotFoundException(exceptionMessage + articleId);
        } else if (article.getPictureUrl() == null) {
            log.error("The photo isn't tied to the article with id: {}", articleId);
            throw new PhotoException(exceptionMessage3 + articleId);
        }

        manager.deleteFileByUserId(articleId, "blog");
        log.info("Updating article with id: {}", articleId);
        article.setPictureUrl(url);
        articleDao.update(article);
        return mapper.map(article, MainArticleDto.class);
    }

}
