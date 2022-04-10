package com.epam.spsa.controller;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.SportTypeService;
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

@RestController
@RequestMapping(path = "/api/v1/sportTypes")
@RequiredArgsConstructor
@Api(tags = "Sport type")
public class SportTypeController {

    private final SportTypeService sportTypeService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "View a sport type by sport type id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sport type found", response = SportTypeDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public SportTypeDto getSportTypeById(@PathVariable int id) {
        return sportTypeService.getSportTypeById(id);
    }

    @GetMapping
    @ApiOperation(value = "View a list of all sport types")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all sport types", response = SportTypeDto.class)
    })
    public List<SportTypeDto> getSportTypes() {
        return sportTypeService.getAllSportTypes();
    }

}