package com.epam.slsa.controller;

import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.comment.CommentDto;
import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.comment.UpdateCommentDto;
import com.epam.slsa.dto.pagination.PaginationDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.CommentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
@Api(tags = "Comment")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("coaches/{coachId}/comment")
    @ApiOperation(value = "Save new comment about coach")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Comment created", response = MainCommentDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainCommentDto save(@Valid @RequestBody CommentDto commentDto,
                               @PathVariable("coachId") int coachId) {
        return commentService.save(commentDto, coachId);
    }

    @DeleteMapping("coaches/{coachId}/comments/{commentId}")
    @ApiOperation(value = "Delete comment by comment id and coach id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void delete(@PathVariable int coachId, @PathVariable int commentId) {
        commentService.delete(coachId, commentId);
    }

    @GetMapping("coaches/{coachId}/comments")
    @ApiOperation(value = "View a list of all comments by coach id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all comments", response = PaginationDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public PaginationDto<MainCommentDto> getAllCommentsByCoachId(@PathVariable("coachId") int coachId,
                                                                 @RequestParam int page, @RequestParam int size) {
        return commentService.getAllByCoachId(coachId, page, size);
    }

    @PutMapping("coaches/{coachId}/comments/{commentId}")
    @ApiOperation(value = "Update comment by comment id and coach id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comment found and updated", response = CoachDto.class),
            @ApiResponse(code = 400, message = "Invalid value(s) for update", response = ApiError.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainCommentDto update(@Valid @RequestBody UpdateCommentDto editedCommentDto,
                                 @PathVariable("coachId") int coachId,
                                 @PathVariable("commentId") int commentId) {
        return commentService.update(editedCommentDto, coachId, commentId);
    }

    @DeleteMapping("coaches/comments/user/{userId}")
    @ApiOperation(value = "Delete comments by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Comments found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void deleteCommentByUserId(@PathVariable("userId") int userId) {
        commentService.deleteByUserId(userId);
    }

}
