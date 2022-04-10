package com.epam.slsa.controller;

import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.SportTypeService;
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
@RequestMapping("api/v1/sportTypes")
@Api(tags = "Sport type")
public class SportTypeController {

    private final SportTypeService sportTypeService;

    @GetMapping("/{id}")
    @ApiOperation(value = "View a sport type by sport type id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Sport type found", response = SportTypeDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public SportTypeDto getById(@PathVariable int id) {
        return sportTypeService.getById(id);
    }

    @GetMapping
    @ApiOperation(value = "View a list of location type")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all sport type", response = SportTypeDto.class)
    })
    public List<SportTypeDto> getAll() {
        return sportTypeService.getAll();
    }

}
