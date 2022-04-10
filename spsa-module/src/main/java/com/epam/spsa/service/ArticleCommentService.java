package com.epam.spsa.service;

import com.epam.spsa.dto.article.ArticleCommentDto;
import com.epam.spsa.dto.article.MainArticleCommentDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleCommentService {

    MainArticleCommentDto getComment(int id);

    List<MainArticleCommentDto> getAll();

    List<MainArticleCommentDto> getAllByArticleId(int id, int type);

    void deleteComment(int id);

    MainArticleCommentDto saveCommentWithImage(MultipartFile multipartFile,
                                               ArticleCommentDto comment,
                                               int userId, int articleId);

    MainArticleCommentDto saveComment(ArticleCommentDto comment,
                                      int userId, int articleId);


    MainArticleCommentDto saveReplyCommentWithImage(MultipartFile multipartFile,
                                                    ArticleCommentDto comment,
                                                    int userId, int commentId);

    MainArticleCommentDto saveReplyComment(ArticleCommentDto comment
            , int userId, int commentId);

    MainArticleCommentDto updateCommentWithImage(MultipartFile multipartFile,
                                                 ArticleCommentDto comment,
                                                 int commentId);

    MainArticleCommentDto updateComment(ArticleCommentDto comment,
                                        int commentId);

    MainArticleCommentDto likeComment(int commentId);

    MainArticleCommentDto dislikeComment(int commentId);

}
