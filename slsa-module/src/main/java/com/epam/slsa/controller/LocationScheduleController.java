package com.epam.slsa.controller;

import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.dto.locationSchedule.MainLocationScheduleDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.LocationScheduleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Set;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
@Api(tags = "Location schedule")
public class LocationScheduleController {

    private final LocationScheduleService locationScheduleService;

    @GetMapping(value = "/locations/{id}/locationSchedule")
    @ApiOperation(value = "View a list of location schedule by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all locations", response = LocationScheduleDto.class)
    })
    public Set<MainLocationScheduleDto> getAllByLocationId(@PathVariable("id") int id) {
        return locationScheduleService.getAllByLocationId(id);
    }

    @PostMapping(value = "/locations/{id}/locationSchedule")
    @ApiOperation(value = "Add a list location schedules by location id")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "LocationSchedule created", response = LocationScheduleDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public Set<MainLocationScheduleDto> save(@RequestBody Set<@Valid LocationScheduleDto> locationScheduleDto,
                                             @PathVariable("id") int id) {
        return locationScheduleService.save(locationScheduleDto, id);
    }

    @PutMapping(value = "/locations/{id}/locationSchedule")
    @ApiOperation(value = "Update a list of all schedules by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location schedule found and updated", response = LocationScheduleDto.class),
            @ApiResponse(code = 400, message = "Invalid value(s) for update", response = ApiError.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public Set<LocationScheduleDto> update(@RequestBody Set<@Valid LocationScheduleDto> locationScheduleDto,
                                           @PathVariable("id") int id) {
        return locationScheduleService.update(locationScheduleDto, id);
    }

    @DeleteMapping(value = "/locations/locationSchedule/{scheduleId}")
    @ApiOperation(value = "Delete location schedule")
    @ApiResponses(value = {
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void delete(@PathVariable int scheduleId) {
        locationScheduleService.delete(scheduleId);
    }

}
