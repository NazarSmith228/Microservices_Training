package com.epam.slsa.controller;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.address.DetailedAddressDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/")
@Api(tags = "Address")
public class AddressController {

    private final AddressService addressService;

    @GetMapping("locations/{id}/address")
    @ApiOperation(value = "View a list of all addresses by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Address found", response = AddressDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public AddressDto getByLocationId(@PathVariable("id") int id) {
        return addressService.getByLocationId(id);
    }

    @GetMapping(value = "addresses/coordinates")
    @ApiOperation(value = "View an address by  latitude and longitude")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Address found", response = DetailedAddressDto.class),
            @ApiResponse(code = 404, message = "Address was not found by such coordinates", response = ApiError.class)
    })
    public DetailedAddressDto getByCoordinates(@RequestParam("lat") double lat,
                                               @RequestParam("long") double lng) {
        return addressService.getByCoordinates(lat, lng);
    }

}
