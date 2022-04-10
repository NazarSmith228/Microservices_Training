package com.epam.spsa.feign.dto.location;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.feign.dto.address.AddressDto;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.locationSchedule.LocationScheduleDto;
import com.epam.spsa.feign.dto.locationType.LocationTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainLocationDto {

    private int id;
    private String name;
    private AddressDto address;
    private LocationTypeDto locationType;
    private Set<LocationScheduleDto> locationSchedule;
    private List<MainCoachDto> coaches;
    private Set<SportTypeDto> sportTypes = new HashSet<>();

}
