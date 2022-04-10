package com.epam.spsa.service.impl;

import com.epam.spsa.dao.EventDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.model.Event;
import com.epam.spsa.model.User;
import com.epam.spsa.service.EventService;
import com.epam.spsa.service.UserService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@Service
public class EventServiceImpl implements EventService {

    private final EventDao eventDao;

    private final UserDao userDao;

    private final ModelMapper modelMapper;

    private final UserService userService;

    private final SimpMessagingTemplate template;

    private final TaskScheduler taskScheduler;

    private final SlsaClient slsaClient;

    @Value("${event.exception.notFound}")
    private String eventNotFoundByIdMessage;

    @Value("${user.exception.notfound}")
    private String userNotFoundByIdMessage;


    public MainEventDto save(EventDto eventDto) {
        log.info("Saving EventDto: {}", eventDto);
        Event event = modelMapper.map(eventDto, Event.class);

        if (eventDto.getLocation_id() != 0) {
            try {
                slsaClient.getById(event.getLocation_id());
            } catch (FeignException.NotFound e) {
                throw new EntityNotFoundException("Non-found location with id=" + event.getLocation_id());
            }
        }

        if (eventDto.getUserIdList() != null) {
            event.setAttendee(eventDto.getUserIdList()
                    .stream()
                    .map(this::getUserById)
                    .collect(Collectors.toList()));
        }
        event.setOwner(getUserById(eventDto.getOwner_id()));

        eventDao.save(event);
        return modelMapper.map(event, MainEventDto.class);
    }

    public MainEventDto update(EventDto eventDto, int id) {
        log.info("Updating EventDto: {}, id: {}", eventDto, id);
        Event oldEvent = getEventById(id);
        log.info("Old event: {}", oldEvent);

        if (eventDto.getLocation_id() != 0) {
            try {
                slsaClient.getById(eventDto.getLocation_id());
            } catch (FeignException.NotFound e) {
                throw new EntityNotFoundException("Non-found location with id=" + eventDto.getLocation_id());
            }
        }

        modelMapper.map(eventDto, oldEvent);

        if (eventDto.getUserIdList() != null) {
            oldEvent.setAttendee(eventDto.getUserIdList()
                    .stream()
                    .map(this::getUserById)
                    .collect(Collectors.toList()));
        }

        return modelMapper.map(eventDao.update(oldEvent), MainEventDto.class);
    }

    public void delete(int id) {
        Event event = getEventById(id);
        User owner = event.getOwner();
        if (!userService.currentUser().getEmail()
                .equals(owner.getEmail())) {
            throw new EntityNotFoundException("This is not owner");
        }
        owner.getOwnEvents().remove(event);
        log.info("Deleting event: {}", event);
        userDao.update(owner);
    }

    public MainEventDto getById(int id) {
        return modelMapper.map(getEventById(id), MainEventDto.class);
    }

    @Override
    public List<MainEventDto> getByUserId(int id) {
        log.info("Getting event by user id: {}", id);
        User user = getUserById(id);
        List<Event> events = user.getOwnEvents();
        events.removeAll(user.getEvents());
        events.addAll(user.getEvents());

        return events.stream()
                .map(e -> modelMapper.map(e, MainEventDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MainEventDto> getAll() {
        log.info("Get all events");
        return eventDao.getAll().stream()
                .map(e -> modelMapper.map(e, MainEventDto.class))
                .collect(Collectors.toList());
    }

    public void getNotificationByUserId(int id) {
        log.info("Notify user with id={}", id);
        try {
            getUserById(id);
        } catch (EntityNotFoundException ex) {
            template.convertAndSend("/spsa/notifications/" + id, ex);
        }
        List<Event> events = getEventsByUserId(id);
        for (Event event : events) {
            if (event.getDate().equals(LocalDate.now())) {
                LocalDateTime date;
                if (event.getTime().toLocalTime().compareTo(LocalTime.now().plusMinutes(15)) > 0) {
                    date = LocalDateTime.of(event.getDate(),
                            event.getTime().toLocalTime().minusMinutes(15));
                } else if (event.getTime().toLocalTime().compareTo(LocalTime.now()) > 0) {
                    date = LocalDateTime.now().plusSeconds(4);
                } else {
                    continue;
                }
                taskScheduler.schedule(() -> {
                    template.convertAndSend("/spsa/notifications/" + id,
                            modelMapper.map(event, MainEventDto.class));
                }, Date.from(date.atZone(ZoneId.systemDefault()).toInstant()));
            }
        }
    }

    private List<Event> getEventsByUserId(int id) {
        return eventDao.getAll()
                .stream()
                .filter(e -> (e.getOwner().getId() == id) || (e.getAttendee()
                        .stream().anyMatch(a -> a.getId() == id)))
                .collect(Collectors.toList());
    }

    private Event getEventById(int id) {
        log.info("Getting event by id: {}", id);
        try {
            Event event = eventDao.getById(id);
            log.info("Found event");
            return event;
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            log.info("Non-found event by id: {}", id);
            throw new EntityNotFoundException(eventNotFoundByIdMessage + id);
        }
    }

    private User getUserById(int id) {
        User owner = userDao.getById(id);
        if (owner == null) {
            log.info("Non-found user by id: {}", id);
            throw new EntityNotFoundException(userNotFoundByIdMessage + id);
        }
        return owner;
    }

}
