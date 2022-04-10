package com.epam.spsa.dao;

import com.epam.spsa.model.ArticleComment;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ArticleCommentDao extends MainDao<ArticleComment> {

    List<ArticleComment> getAllByArticleId(int id);

    List<ArticleComment> getAllByArticleIdASC(int id);

    List<ArticleComment> getAllByArticleIdDESK(int id);

    void deleteComment(int id);

    ArticleComment update(ArticleComment articleComment);

    ArticleComment getDeletedComment();

}
