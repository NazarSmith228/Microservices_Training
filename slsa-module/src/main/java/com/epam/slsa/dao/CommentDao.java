package com.epam.slsa.dao;

import com.epam.slsa.model.Comment;

import java.util.List;

public interface CommentDao extends CrudDao<Comment> {

    List<Comment> getAll();

}
