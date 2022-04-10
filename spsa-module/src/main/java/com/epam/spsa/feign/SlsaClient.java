package com.epam.spsa.feign;

import com.epam.spsa.feign.dto.address.DetailedAddressDto;
import com.epam.spsa.feign.dto.coach.CoachDto;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@FeignClient(name = "sport-location-service-api", url = "${slsa.connect.url}")
@Component
public interface SlsaClient {

    @GetMapping("api/v1/locations")
    List<MainLocationDto> getAll();

    @GetMapping("api/v1/locations/{id}")
    MainLocationDto getById(@PathVariable("id") int id);

    @GetMapping("api/v1/addresses/coordinates")
    DetailedAddressDto getByCoordinates(@RequestParam("lat") double lat,
                                        @RequestParam("long") double lng);

    @PostMapping("api/v1/locations/{locationId}/set/admin/{adminId}")
    void setAdmin(@PathVariable int locationId, @PathVariable int adminId);

    @GetMapping("api/v1/admin/{adminId}/locations")
    List<MainLocationDto> getByAdminId(@PathVariable int adminId);

    @PostMapping("api/v1/locations/{id}/set/coach")
    MainCoachDto saveCoachInLocation(@Valid @RequestBody CoachDto newCoach,
                                     @PathVariable("id") int id);

    @DeleteMapping("api/v1/coaches/comments/user/{userId}")
    void deleteCommentByUserId(@PathVariable("userId") int userId);

    @PostMapping("api/v1/coaches/{id}/new-location")
    void setNewLocationByCoachId(@PathVariable int id,
                                 @RequestParam("location_id") int locationId);

    @GetMapping("api/v1/email/coaches")
    MainCoachDto getByEmail(@RequestParam("coach_email") String email);

    @GetMapping("api/v1/coaches/user/{userId}")
    MainCoachDto getCoachByUserId(@PathVariable int userId);

    @DeleteMapping("api/v1/coaches/{coachId}")
    void delete(@PathVariable int coachId);

}