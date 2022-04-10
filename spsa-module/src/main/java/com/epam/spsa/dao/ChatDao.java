package com.epam.spsa.dao;

import com.epam.spsa.model.Chat;

public interface ChatDao extends MainDao<Chat> {

    void delete(Chat chat);

}
