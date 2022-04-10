package com.epam.spsa.controller;

import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.EventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/events")
@RequiredArgsConstructor
@Api(tags = "Event")
@Slf4j
public class EventController {

    private final EventService eventService;

    @PostMapping("/")
    @ApiOperation("Add new event")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "New event created", response = MainEventDto.class),
            @ApiResponse(code = 400, message = "Validation error", response = ApiError.class)
    })
    MainEventDto createEvent(@Valid @RequestBody EventDto eventDto) {
        return eventService.save(eventDto);
    }

    @GetMapping("/")
    @ApiOperation("Get list of all events")
    @ApiResponses({
            @ApiResponse(code = 200, message = "List of all events", response = MainEventDto.class)
    })
    List<MainEventDto> getEvents() {
        return eventService.getAll();
    }

    @GetMapping("/{id}")
    @ApiOperation("Get event by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Event found", response = MainEventDto.class),
            @ApiResponse(code = 404, message = "Event id non-found", response = ApiError.class)
    })
    MainEventDto getEventById(@PathVariable int id) {
        return eventService.getById(id);
    }

    @GetMapping("/user/{id}")
    @ApiOperation("Get event by user id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Events found", response = MainEventDto.class),
            @ApiResponse(code = 404, message = "User id non-found", response = ApiError.class)
    })
    List<MainEventDto> getEventsByUserId(@PathVariable int id) {
        return eventService.getByUserId(id);
    }

    @PutMapping("/{id}")
    @ApiOperation("Edit event by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "event edited", response = MainEventDto.class),
            @ApiResponse(code = 400, message = "Validation error", response = ApiError.class),
            @ApiResponse(code = 404, message = "Event id non-found", response = ApiError.class)
    })
    MainEventDto updateEvent(@Valid @RequestBody EventDto eventDto, @PathVariable int id) {
        return eventService.update(eventDto, id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation("Remove event by id")
    @ApiResponses({
            @ApiResponse(code = 200, message = "Event deleted"),
            @ApiResponse(code = 404, message = "Event id non-found", response = ApiError.class)
    })
    void deleteEvent(@PathVariable int id) {
        eventService.delete(id);
    }

    @MessageMapping("/notifications/{user_id}")
    public void notify(@DestinationVariable int user_id) {
        eventService.getNotificationByUserId(user_id);
    }

}
