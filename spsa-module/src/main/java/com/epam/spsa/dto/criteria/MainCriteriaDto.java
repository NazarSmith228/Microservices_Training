package com.epam.spsa.dto.criteria;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.dto.user.UserDto;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model that represents Criteria. Accessible for GET request")
public class MainCriteriaDto {

    private int id;
    private UserDto user;
    private SportTypeDto sportType;
    private String maturity;
    private double runningDistance;
    private String gender;
    private String activityTime;

}
