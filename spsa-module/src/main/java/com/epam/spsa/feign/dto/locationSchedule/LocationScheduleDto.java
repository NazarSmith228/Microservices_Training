package com.epam.spsa.feign.dto.locationSchedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationScheduleDto {

    private String day;
    private String startTime;
    private String endTime;

}
