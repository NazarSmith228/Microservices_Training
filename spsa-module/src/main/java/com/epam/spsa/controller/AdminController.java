package com.epam.spsa.controller;

import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityRoleException;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import feign.FeignException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/admin")
@RequiredArgsConstructor
@Api(tags = "Admin")
public class AdminController {

    private final SlsaClient slsaClient;

    @Value("${user.exception.notfound}")
    private String userNotFoundMessage;

    @Value("${user.exception.roleAdmin}")
    private String userRoleMessage;

    @GetMapping(value = "/{adminId}/locations")
    @ApiOperation(value = "Get locations for venue admin")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "All locations"),
    })
    public List<MainLocationDto> getLocations(@PathVariable int adminId) {
        try {
            return slsaClient.getByAdminId(adminId);
        } catch (FeignException.NotFound ex) {
            throw new EntityAlreadyExistsException(userNotFoundMessage + adminId);
        } catch (FeignException.BadRequest ex) {
            throw new EntityRoleException(userRoleMessage);
        }
    }

}
