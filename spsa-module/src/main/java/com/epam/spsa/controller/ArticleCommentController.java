package com.epam.spsa.controller;

import com.epam.spsa.dto.article.ArticleCommentDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.ArticleCommentService;
import com.epam.spsa.validation.Image;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/blogs")
@RequiredArgsConstructor
@Validated
@Api(tags = "Article comments")
public class ArticleCommentController {

    private final ArticleCommentService articleCommentService;

    @GetMapping(value = "/comments")
    @ApiOperation(value = "Get list of all comments")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of comments", response = MainArticleCommentDto.class),
    })
    public List<MainArticleCommentDto> getAll() {
        return articleCommentService.getAll();
    }

    @GetMapping(value = "/{id}/comments")
    @ApiOperation(value = "Get list comments by article id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of comments", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public List<MainArticleCommentDto> getAllByArticleId(@PathVariable int id, @RequestParam int type) {
        return articleCommentService.getAllByArticleId(id, type);
    }

    @GetMapping(value = "/comments/{id}")
    @ApiOperation(value = "Get comment by comment id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment found", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainArticleCommentDto getComment(@PathVariable int id) {
        return articleCommentService.getComment(id);
    }

    @DeleteMapping(value = "/comments/{id}")
    @ApiOperation(value = "Delete comment by comment id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Deleted comment"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void deleteCommentById(@PathVariable int id) {
        articleCommentService.deleteComment(id);
    }

    @PostMapping(value = "/{blogId}/comments-img/user/{userId}")
    @ApiOperation(value = "Add new comment with image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created new comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleCommentDto saveCommentWithImage(@PathVariable int blogId, @PathVariable int userId,
                                                      @RequestParam("file") @Image MultipartFile image,
                                                      @RequestParam @Valid ArticleCommentDto commentDto) {
        return articleCommentService.saveCommentWithImage(image, commentDto, userId, blogId);
    }

    @PostMapping(value = "/{blogId}/comments/user/{userId}")
    @ApiOperation(value = "Add new comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created new comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleCommentDto saveComment(@PathVariable int blogId, @PathVariable int userId,
                                             @RequestParam @Valid ArticleCommentDto commentDto) {
        return articleCommentService.saveComment(commentDto, userId, blogId);
    }

    @PostMapping(value = "/reply-comments-img/{commentId}/user/{userId}")
    @ApiOperation(value = "Add new reply comment with image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created new reply comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleCommentDto saveReplyCommentWithImage(@PathVariable int commentId, @PathVariable int userId,
                                                           @RequestParam("file") @Image MultipartFile image,
                                                           @RequestParam @Valid ArticleCommentDto commentDto) {
        return articleCommentService.saveReplyCommentWithImage(image, commentDto, userId, commentId);
    }

    @PostMapping(value = "/reply-comments/{commentId}/user/{userId}")
    @ApiOperation(value = "Add new reply comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Created new reply comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainArticleCommentDto saveReplyComment(@PathVariable int commentId, @PathVariable int userId,
                                                  @RequestParam @Valid ArticleCommentDto commentDto) {
        return articleCommentService.saveReplyComment(commentDto, userId, commentId);
    }

    @PutMapping(value = "/comments-img/{commentId}")
    @ApiOperation(value = "Update comment with image")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainArticleCommentDto updateCommentWithImage(@PathVariable int commentId,
                                                        @RequestParam("file") @Image MultipartFile image,
                                                        @RequestParam @Valid ArticleCommentDto commentDto) {
        return articleCommentService.updateCommentWithImage(image, commentDto, commentId);
    }

    @PutMapping(value = "/comments/{commentId}")
    @ApiOperation(value = "Update comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainArticleCommentDto updateComment(@PathVariable int commentId,
                                               @RequestParam @Valid ArticleCommentDto commentDto) {
        return articleCommentService.updateComment(commentDto, commentId);
    }

    @PostMapping(value = "/comments/{commentId}/inc")
    @ApiOperation(value = "Increment rating of comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainArticleCommentDto likeComment(@PathVariable int commentId) {
        return articleCommentService.likeComment(commentId);
    }

    @PostMapping(value = "/comments/{commentId}/dec")
    @ApiOperation(value = "Decrement rating of comment")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Updated comment", response = MainArticleCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainArticleCommentDto dislikeComment(@PathVariable int commentId) {
        return articleCommentService.dislikeComment(commentId);
    }

}
