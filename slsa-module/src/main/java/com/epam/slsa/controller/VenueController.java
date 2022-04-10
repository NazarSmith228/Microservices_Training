package com.epam.slsa.controller;

import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.location.CriteriaLocationDto;
import com.epam.slsa.service.VenueService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/api/v1/locations")
@RequiredArgsConstructor
@Api(tags = "Venue")
public class VenueController {

    private final VenueService venueService;

    @PostMapping("/criteria/venues")
    @ApiOperation(value = "View matched list of venues")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all suitable venues", response = CriteriaLocationDto.class)
    })
    public List<CriteriaLocationDto> match(@Valid @RequestBody CriteriaDto criteriaDto) {
        return venueService.getSuitableLocation(criteriaDto);
    }

}
