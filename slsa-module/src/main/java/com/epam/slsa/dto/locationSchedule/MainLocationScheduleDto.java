package com.epam.slsa.dto.locationSchedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainLocationScheduleDto {

    private int id;
    private String day;
    private String startTime;
    private String endTime;

}
