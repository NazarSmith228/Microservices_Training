package com.epam.spsa.dto.user;

import com.epam.spsa.model.SportType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MainCommonUserStatDto {

    private double resultKm;
    private String resultH;
    private Set<SportType> sportTypes;
    private Set<Integer> locations;
    private Set<Integer> coaches;
}
