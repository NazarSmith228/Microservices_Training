package com.epam.spsa.service;

import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.dto.event.MainEventDto;

import java.util.List;

public interface EventService {

    List<MainEventDto> getAll();

    MainEventDto getById(int id);

    List<MainEventDto> getByUserId(int id);

    void getNotificationByUserId(int id);

    MainEventDto save(EventDto eventDto);

    MainEventDto update(EventDto eventDto, int id);

    void delete(int id);

}
