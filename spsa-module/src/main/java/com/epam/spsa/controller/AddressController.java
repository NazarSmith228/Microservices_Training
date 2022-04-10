package com.epam.spsa.controller;

import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.address.DetailedAddressDto;
import com.epam.spsa.error.ApiError;
import com.epam.spsa.service.AddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/api/v1")
@RequiredArgsConstructor
@Api(tags = "Address")
@Slf4j
public class AddressController {

    private final AddressService addressService;

    @GetMapping(value = "/users/{id}/address")
    @ApiOperation(value = "View an address by user id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Address found", response = AddressDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public AddressDto getAddressByUserId(@PathVariable int id) {
        return addressService.getAddressByUserId(id);
    }

    @GetMapping(value = "/addresses")
    @ApiOperation(value = "View a list of all addresses")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all addresses", response = AddressDto.class)
    })
    public List<AddressDto> getAllAddress() {
        return addressService.getAllAddresses();
    }

    @GetMapping(value = "/addresses/coordinates", params = {"lat", "long"})
    @ApiOperation(value = "View all addresses by latitude and longitude")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Address", response = DetailedAddressDto.class),
            @ApiResponse(code = 404, message = "Address was not found by such coordinates", response = ApiError.class)
    })
    public List<DetailedAddressDto> getAddressByCoordinates(@RequestParam("lat") double latitude,
                                                            @RequestParam("long") double longitude) {
        return addressService.getAddressByCoordinates(latitude, longitude);
    }

}