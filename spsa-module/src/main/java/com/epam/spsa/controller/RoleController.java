package com.epam.spsa.controller;

import com.epam.spsa.dto.security.RoleDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.RoleService;
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
@RequestMapping(path = "/api/v1/roles")
@RequiredArgsConstructor
@Api(tags = "Role")
public class RoleController {

    private final RoleService roleService;

    @GetMapping(value = "/{id}")
    @ApiOperation(value = "View a role by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role found", response = RoleDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public RoleDto getById(@PathVariable int id) {
        return roleService.getById(id);
    }

    @GetMapping
    @ApiOperation(value = "View a list of all roles")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all sport types", response = RoleDto.class)
    })
    public List<RoleDto> getAll() {
        return roleService.getAll();
    }

}
