package com.epam.slsa.controller;

import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.error.ApiError;
import com.epam.slsa.service.LocationService;
import com.epam.slsa.validation.Image;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1")
@Api(tags = "Location")
@Validated
public class LocationController {

    private final LocationService locationService;

    @GetMapping(value = "/locations")
    @ApiOperation(value = "View a list of all locations")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "List of all locations", response = MainLocationDto.class)
    })
    public List<MainLocationDto> getAll() {
        return locationService.getAll();
    }

    @GetMapping(value = "/locations/{id}")
    @ApiOperation(value = "View a location by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found", response = MainLocationDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainLocationDto getById(@PathVariable("id") int id) throws Exception {
        return locationService.getById(id);
    }

    @PostMapping(value = "/locations")
    @ApiOperation(value = "Save new location")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Location created", response = MainLocationDto.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainLocationDto save(@Valid @RequestBody LocationDto newLocationDto) {
        return locationService.save(newLocationDto);
    }

    @PutMapping(value = "/locations/{id}")
    @ApiOperation(value = "Update location by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found and updated", response = LocationDto.class),
            @ApiResponse(code = 400, message = "Invalid value(s) for update", response = ApiError.class),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public LocationDto update(@Valid @RequestBody LocationDto locationDto,
                              @PathVariable("id") int id) {
        return locationService.update(locationDto, id);
    }

    @DeleteMapping(value = "/locations/{id}")
    @ApiOperation(value = "Delete location by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void delete(@PathVariable("id") int id) {
        locationService.delete(id);
    }

    @PostMapping(value = "/locations/{id}/images")
    @ApiOperation(value = "Add new image to location by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found and got new image"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    @ResponseStatus(HttpStatus.CREATED)
    public MainLocationDto saveImage(@RequestParam("file") @Image MultipartFile image, @PathVariable int id) {
        return locationService.saveImage(image, id);
    }

    @PostMapping(value = "/locations/{id}/url-images")
    @ApiOperation(value = "Add new url-images to location by location id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found and got new images"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainLocationDto saveUrlImage(@RequestParam String[] url, @PathVariable int id) {
        return locationService.saveImages(url, id);
    }

    @DeleteMapping(value = "/locations/{locationId}/images/{imageId}")
    @ApiOperation(value = "Deleted image by location and image id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Image found and deleted"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public void deleteImage(@PathVariable int locationId, @PathVariable int imageId) {
        locationService.deleteImage(locationId, imageId);
    }

    @PutMapping(value = "/locations/{locationId}/url-images/{imageId}")
    @ApiOperation(value = "Update url-image by location id and image id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found and updated image"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainLocationDto updateUrlImage(@RequestParam String url,
                                          @PathVariable int locationId,
                                          @PathVariable int imageId) {
        return locationService.updateImage(url, locationId, imageId);
    }

    @PutMapping(value = "/locations/{locationId}/images/{imageId}")
    @ApiOperation(value = "Update image by location id and image id")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Location found and updated image"),
            @ApiResponse(code = 404, message = "Non-existing id", response = ApiError.class)
    })
    public MainLocationDto updateImage(@RequestParam("file") @Image MultipartFile image,
                                       @PathVariable int locationId,
                                       @PathVariable int imageId) {
        return locationService.updateImage(image, locationId, imageId);
    }

    @PostMapping(value = "/locations/{locationId}/set/admin/{adminId}")
    @ApiOperation(value = "Set admin to location")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Admin has been set"),
            @ApiResponse(code = 404, message = "Location not found", response = ApiError.class),
            @ApiResponse(code = 404, message = "User not found", response = ApiError.class),
            @ApiResponse(code = 400, message = "User doesn't have admin role", response = ApiError.class)
    })
    public void setAdmin(@PathVariable int locationId, @PathVariable int adminId) {
        locationService.setAdmin(locationId, adminId);
    }

    @GetMapping(value = "/admin/{adminId}/locations")
    @ApiOperation(value = "List of admin's locations")
    public List<MainLocationDto> getByAdminId(@PathVariable int adminId) {
        return locationService.getByAdminId(adminId);
    }

}