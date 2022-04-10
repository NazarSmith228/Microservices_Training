package com.epam.spsa.controller;

import com.epam.spsa.dto.article.ArticleDto;
import com.epam.spsa.dto.article.MainArticleDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.error.exception.PhotoException;
import com.epam.spsa.service.ArticleService;
import com.epam.spsa.validation.Image;
import com.epam.spsa.validation.validators.ValidationGroups;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(path = "/api/v1/blogs")
@RequiredArgsConstructor
@Validated
@Api(tags = "Blog")
public class BlogController {

    private final ArticleService articleService;

    @PostMapping(value = "/create")
    @ApiOperation(value = "Add new article to blog", response = MainArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Article created", response = MainArticleDto.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleDto createArticle(@RequestBody
                                        @Validated({ValidationGroups.ArticleInsert.class})
                                                ArticleDto articleDto) {
        return articleService.save(articleDto);
    }

    @PutMapping(value = "/update/{id}")
    @ApiOperation(value = "Update Article", response = MainArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article updated", response = MainArticleDto.class),
            @ApiResponse(code = 400, message = "Bad request", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.OK)
    public MainArticleDto updateArticle(@RequestBody @Validated({ValidationGroups.ArticleUpdate.class})
                                                ArticleDto articleDto, @PathVariable int id) {
        return articleService.update(articleDto, id);
    }

    @GetMapping(value = "/articles/{id}")
    @ApiOperation(value = "Get article by id", response = MainArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article found"),
            @ApiResponse(code = 404, message = "Article not found")
    })
    @ResponseStatus(HttpStatus.OK)
    public MainArticleDto getArticleById(@PathVariable int id) {
        return articleService.getById(id);
    }

    @GetMapping(value = "", params = {"page", "size"})
    @ApiOperation(value = "Get Paginated List of Articles")
    @ResponseStatus(HttpStatus.OK)
    public PaginationDto<MainArticleDto> getAll(@RequestParam int page, @RequestParam int size) {
        return articleService.getAllPaginated(page, size);
    }

    @DeleteMapping(value = "/delete/{id}")
    @ApiOperation(value = "Delete Article by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article deleted"),
            @ApiResponse(code = 404, message = "Article not found")
    })
    @ResponseStatus(HttpStatus.OK)
    public void deleteArticleById(@PathVariable int id) {
        articleService.delete(id);
    }

    @PostMapping(value = "/{id}/image")
    @ApiOperation(value = "Save article's image file by article id", response = MainArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Article found and attached to the photo"),
            @ApiResponse(code = 400, message = "The photo is already tied to the article", response = PhotoException.class),
            @ApiResponse(code = 404, message = "Non-existing article id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleDto saveImageByArticleId(@RequestParam("file") @Image MultipartFile image, @PathVariable int id) {
        return articleService.saveImageByArticleId(image, id);
    }

    @PostMapping(value = "/{id}/image-url")
    @ApiOperation(value = "Save article's URL of image by article id", response = MainArticleDto.class)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Article found and attached to the photo"),
            @ApiResponse(code = 400, message = "The photo is already tied to the article", response = PhotoException.class),
            @ApiResponse(code = 404, message = "Non-existing article id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleDto saveUrlImageByArticleId(@RequestParam String url, @PathVariable int id) {
        return articleService.saveImageByArticleId(url, id);
    }

    @DeleteMapping(value = "/{id}/image")
    @ApiOperation(value = "Delete article's image by article id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article's photo deleted"),
            @ApiResponse(code = 404, message = "Non-existing article id", response = ApiError.class)
    })
    public void deleteImageByArticleId(@PathVariable int id) {
        articleService.deleteImageByArticleId(id);
    }

    @PutMapping(value = "/{id}/image")
    @ApiOperation(value = "Update article's image by article id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article's photo deleted"),
            @ApiResponse(code = 404, message = "Non-existing article id", response = ApiError.class)
    })
    public MainArticleDto updateImageByArticleId(@RequestParam("file") @Image MultipartFile image, @PathVariable int id) {
        return articleService.updateImageByArticleId(image, id);
    }

    @PutMapping(value = "/{id}/image-url")
    @ApiOperation(value = "Update article's url-image by article id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Article's photo deleted"),
            @ApiResponse(code = 404, message = "Non-existing article id", response = ApiError.class)
    })
    public MainArticleDto updateUrlImageByArticleId(@RequestParam String url, @PathVariable int id) {
        return articleService.updateImageByArticleId(url, id);
    }

}
