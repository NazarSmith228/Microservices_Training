package com.epam.spsa.controller;

import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.FormService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(path = "api/v1/forms")
@RequiredArgsConstructor
@Api(tags = "Form")
@Validated
public class FormController {

    private final FormService formService;

    @GetMapping
    @ApiOperation(value = "View paginated list of forms")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of forms", response = MainCriteriaDto.class)
    })
    public PaginationDto<MainFormDto> getAll(@RequestParam("pageNumber") int pageNumber,
                                             @RequestParam("pageSize") int pageSize) {
        return formService.getAll(pageNumber, pageSize);
    }

    @PostMapping
    @ApiOperation(value = "Save form")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Form created", response = Map.class),
            @ApiResponse(code = 404, message = "Location was not found", response = Map.class),
            @ApiResponse(code = 400, message = "Form for this role already exists", response = Map.class),
            @ApiResponse(code = 400, message = "Unsupported role", response = Map.class),
    })
    public Map<String, Integer> save(@Valid @RequestBody FormDto formDto) {
        Map<String, Integer> resp = new HashMap<>();
        resp.put("id", formService.save(formDto));
        return resp;
    }

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "View form by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Form found", response = MainFormDto.class),
            @ApiResponse(code = 404, message = "Non-existing form id", response = ApiError.class)
    })
    public MainFormDto getById(@PathVariable int id) {
        return formService.getById(id);
    }

    @GetMapping(value = "/approve/{id}")
    @ApiOperation(value = "Approve user form")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Form approved"),
            @ApiResponse(code = 404, message = "Non-existing form id", response = ApiError.class)
    })
    public void approve(@PathVariable int id) {
        formService.approve(id);
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "Delete form")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Form approved"),
            @ApiResponse(code = 404, message = "Non-existing form id", response = ApiError.class),
            @ApiResponse(code = 403, message = "User is trying to delete not his form", response = ApiError.class)
    })
    public void delete(@PathVariable int id) {
        formService.delete(id);
    }

}
