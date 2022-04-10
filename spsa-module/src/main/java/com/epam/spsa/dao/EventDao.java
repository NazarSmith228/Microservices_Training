package com.epam.spsa.dao;

import com.epam.spsa.model.Event;

public interface EventDao extends MainDao<Event> {

    void delete(Event event);

    Event update(Event event);

}
