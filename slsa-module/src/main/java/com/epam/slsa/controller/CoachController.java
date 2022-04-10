package com.epam.slsa.controller;

import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coach.MainCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.CoachService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
@Validated
@Api(tags = "Coach")
public class CoachController {

    private final CoachService coachService;

    @GetMapping("locations/{id}/coaches")
    @ApiOperation(value = "View a list of all coaches by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all coaches", response = MainCoachDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public List<MainCoachDto> getAllByLocationId(@PathVariable("id") int locationId) {
        return coachService.getAllByLocationId(locationId);
    }


    @GetMapping("locations/{id}/coaches/{coachId}")
    @ApiOperation(value = "View a coach by location and coach id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Coach found", response = MainCoachDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainCoachDto getByIdAndLocationId(@PathVariable("id") int id, @PathVariable("coachId") int coachId) {
        return coachService.getByIdAndLocationId(coachId, id);
    }

    @GetMapping("coaches/user/{userId}")
    @ApiOperation(value = "Get coach by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Coach found", response = MainCoachDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainCoachDto getByUserId(@PathVariable int userId) {
        return coachService.getByUserId(userId);
    }

    @PostMapping("locations/{id}/set/coach")
    @ApiOperation(value = "Put new coach by location id")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Coach created", response = CoachDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiError.class),
            @ApiResponse(code = 404, message = "User doesn't have coach role", response = ApiError.class)
    })
    public MainCoachDto saveCoachInLocation(@Valid @RequestBody CoachDto newCoach,
                                            @PathVariable("id") int id) {
        return coachService.save(newCoach, id);
    }

    @PutMapping("locations/{id}/coaches/{coachId}")
    @ApiOperation(value = "Update coach by location and coach id", notes = "userId filed won't be updated")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Coach found and updated", response = CoachDto.class),
            @ApiResponse(code = 400, message = "Invalid value(s) for update", response = ApiError.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainCoachDto updateCoachInLocation(@Valid @RequestBody CoachDto editedCoachDto,
                                              @PathVariable("id") int id,
                                              @PathVariable("coachId") int coachId) {
        return coachService.update(editedCoachDto, id, coachId);
    }

    @DeleteMapping("coaches/{coachId}")
    @ApiOperation(value = "Delete coach by coach id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Coach found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void delete(@PathVariable int coachId) {
        coachService.delete(coachId);
    }

    @DeleteMapping("coaches/links/{linkId}")
    @ApiOperation(value = "Delete link by link id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Link found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void deleteLink(@PathVariable int linkId) {
        coachService.deleteCoachLinkById(linkId);
    }

    @PostMapping("coaches/{id}/new-location")
    @ApiOperation(value = "Set new location to the coach")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New location has been set"),
            @ApiResponse(code = 404, message = "Non-existing coach id/location id", response = ApiError.class)
    })
    public void setNewLocationByCoachId(@PathVariable int id,
                                        @RequestParam("location_id") int locationId) {
        coachService.setNewLocationByCoachId(locationId, id);
    }

    @PostMapping("coaches/match")
    @ApiOperation(value = "Find coaches by criteria")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of suitable coaches"),
    })
    public List<CriteriaCoachDto> findCoach(@Valid @RequestBody CoachCriteriaDto criteriaDto) {
        return coachService.getSuitableCoach(criteriaDto);
    }

}
