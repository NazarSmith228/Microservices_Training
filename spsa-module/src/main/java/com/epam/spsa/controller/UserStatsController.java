package com.epam.spsa.controller;

import com.epam.spsa.dto.user.CommonUserStatDto;
import com.epam.spsa.dto.user.MainCommonUserStatDto;
import com.epam.spsa.dto.user.MainUserStatsDto;
import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.service.UserStatsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/user")
@RequiredArgsConstructor
@Api(tags = "User Statistic")
public class UserStatsController {

    private final UserStatsService statsService;

    @PostMapping("/{id}/stats")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Save user's statistic")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "User's statistic saved", response = MainUserStatsDto.class),
            @ApiResponse(code = 404, message = "Non-existing user or location or coach")
    })
    public MainUserStatsDto saveUserStats(@RequestBody @Valid UserStatsDto userStatsDto, @PathVariable int id) {
        return statsService.saveUserStats(userStatsDto, id);
    }

    @PutMapping("/{id}/stats")
    @ApiOperation(value = "Update last user's statistic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's statistic found", response = MainUserStatsDto.class),
            @ApiResponse(code = 404, message = "Non-existing user or location or coach")
    })
    public MainUserStatsDto updateLastUserStats(@RequestBody @Valid UserStatsDto stats, @PathVariable int id) {
        return statsService.updateLastUserStats(stats, id);
    }

    @GetMapping("/{id}/stats")
    @ApiOperation(value = "Get all user's statistics")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's statistic found"),
            @ApiResponse(code = 404, message = "Non-existing user")
    })
    public List<MainUserStatsDto> getAllStatsById(@PathVariable int id) {
        return statsService.getAllUserStats(id);
    }

    @GetMapping("/{id}/stats/last")
    @ApiOperation(value = "Get last user's statistic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's statistic found", response = MainUserStatsDto.class),
            @ApiResponse(code = 404, message = "Non-existing user")
    })
    public MainUserStatsDto getLastUserStats(@PathVariable(name = "id") int userId) {
        return statsService.getLastUserStats(userId);
    }

    @PostMapping("/{id}/common-stats")
    @ApiOperation(value = "View user's common statistic")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User's statistic found"),
            @ApiResponse(code = 404, message = "Non-existing user")
    })
    public MainCommonUserStatDto getCommonUserStat(@PathVariable int id,
                                                   @RequestBody @Valid CommonUserStatDto commonUserStatDto) {
        return statsService.getCommonStat(commonUserStatDto, id);
    }

}
