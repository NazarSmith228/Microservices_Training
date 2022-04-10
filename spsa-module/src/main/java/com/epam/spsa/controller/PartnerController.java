package com.epam.spsa.controller;

import com.epam.spsa.dto.criteria.AllPartnersDto;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.PartnerService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(path = "/api/v1/users")
@RequiredArgsConstructor
@Api(tags = "Partner")
public class PartnerController {

    private final PartnerService partnerService;

    @PostMapping(value = "/{id}/partners")
    @ApiOperation(value = "View list of partners")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all suitable partners", response = AllPartnersDto.class),
            @ApiResponse(code = 400, message = "User profile is not full", response = ApiError.class),
            @ApiResponse(code = 404, message = "No users match", response = ApiError.class)
    })
    public PaginationDto<CriteriaUserDto> match(@Valid @RequestBody CriteriaDto criteriaDto, @PathVariable int id,
                                                @RequestParam("pageNumber") int pageNumber, @RequestParam("pageSize") int pageSize) {
        return partnerService.getSuitablePartner(criteriaDto, id, pageNumber, pageSize);
    }

    @PostMapping(value = "/{id}/criteria")
    @ApiOperation(value = "Save criteria")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Criteria created", response = Map.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public Map<String, Integer> save(@Valid @RequestBody CriteriaDto criteriaDto, @PathVariable int id) {
        Map<String, Integer> resp = new HashMap<>();
        resp.put("id", partnerService.save(criteriaDto, id));
        return resp;
    }

    @GetMapping(value = "/criteria")
    @ApiOperation(value = "View list of criteria")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all criteria", response = MainCriteriaDto.class)
    })
    public List<MainCriteriaDto> getAll() {
        return partnerService.getAll();
    }

    @GetMapping(value = "/{id}/criteria")
    @ApiOperation(value = "View list of criteria by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of user`s criteria", response = CriteriaDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public List<MainCriteriaDto> getByUserId(@PathVariable int id) {
        return partnerService.getCriteriaByUserId(id);
    }

    @DeleteMapping(value = "/{userId}/criteria/{criteriaId}")
    @ApiOperation(value = "Delete criteria by id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Criteria found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void deleteCriteria(@PathVariable int userId, @PathVariable int criteriaId) {
        partnerService.delete(userId, criteriaId);
    }

}