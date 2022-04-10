package com.epam.spsa.dao;

import com.epam.spsa.model.Message;

public interface MessageDao extends MainDao<Message> {

    void delete(Message message);

    Message update(Message message);

}
