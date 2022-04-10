package com.epam.spsa.dao;

import com.epam.spsa.model.Tag;

public interface TagDao extends MainDao<Tag> {

    void delete(Tag tag);

    Tag getByName(String name);

}
