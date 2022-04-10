package com.epam.spsa.dto.user;

import com.epam.spsa.dto.sport.SportTypeDto;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class MainUserStatsDto {

    private int userId;
    private SportTypeDto sportType;
    private double resultKm;
    private String resultH;
    private int locationId;
    private int coachId;
    private LocalDate insertionDate;
}
