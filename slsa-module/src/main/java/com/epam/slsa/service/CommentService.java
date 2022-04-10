package com.epam.slsa.service;

import com.epam.slsa.dto.comment.CommentDto;
import com.epam.slsa.dto.comment.MainCommentDto;
import com.epam.slsa.dto.comment.UpdateCommentDto;
import com.epam.slsa.dto.pagination.PaginationDto;

public interface CommentService {

    MainCommentDto save(CommentDto commentDto, int coachId);

    void delete(int coachId, int commentId);

    PaginationDto<MainCommentDto> getAllByCoachId(int coachId, int pageNumber, int pageSize);

    MainCommentDto update(UpdateCommentDto editedCommentDto, int coachId, int commentId);

    void deleteByUserId(int userId);

}
