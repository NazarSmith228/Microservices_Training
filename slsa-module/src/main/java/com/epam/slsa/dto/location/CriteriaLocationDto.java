package com.epam.slsa.dto.location;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.image.MainImageDto;
import com.epam.slsa.dto.locationSchedule.MainLocationScheduleDto;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CriteriaLocationDto {

    private int id;
    private String name;
    private AddressDto address;
    private Integer adminId;
    private LocationTypeDto locationType;
    private String webSite;
    private String phoneNumber;
    private Set<MainLocationScheduleDto> locationSchedule;
    private List<CriteriaCoachDto> coaches;
    private List<MainImageDto> photos;
    private Set<SportTypeDto> sportTypes;

}
