package com.epam.slsa.controller;

import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.LocationTypeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/locationTypes")
@Api(tags = "Location type")
public class LocationTypeController {

    private final LocationTypeService locationTypeService;

    @GetMapping("/{id}")
    @ApiOperation(value = "View a location type by location type id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location type found", response = LocationTypeDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public LocationTypeDto getById(@PathVariable int id) {
        return locationTypeService.getById(id);
    }

    @GetMapping
    @ApiOperation(value = "View a list of location type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all location type", response = LocationTypeDto.class)
    })
    public List<LocationTypeDto> getAll() {
        return locationTypeService.getAll();
    }

}
