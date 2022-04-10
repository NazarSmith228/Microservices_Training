package com.epam.spsa.dto.criteria;

import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.validation.ActivityTimeValue;
import com.epam.spsa.validation.GenderType;
import com.epam.spsa.validation.MaturityType;
import com.epam.spsa.validation.SportTypeValue;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model that represents Criteria. Accessible for POST and specific GET methods")
public class CriteriaDto {

    @SportTypeValue
    private SportTypeDto sportType;

    @MaturityType
    @ApiModelProperty(example = "PRO", notes = "Only: beginner, middle, pro")
    private String maturity;

    @Min(value = 0)
    @Max(value = 50)
    @ApiModelProperty(example = "15", notes = "minimum 0 maximum 50")
    private double runningDistance;

    @GenderType
    @ApiModelProperty(example = "male", notes = "Only latin letter,Only: male, both, female")
    private String gender;

    @ActivityTimeValue
    @ApiModelProperty(example = "Morning", notes = "Only: morning, noon, evening, all")
    private String activityTime;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        CriteriaDto that = (CriteriaDto) o;
        return Double.compare(that.runningDistance, runningDistance) == 0
                && equals(sportType, that.sportType)
                && equals(maturity, that.maturity)
                && equals(gender, that.gender)
                && equals(activityTime, that.activityTime);
    }

    private boolean equals(Object a, Object b) {
        if (a instanceof String && b instanceof String) {
            a = ((String) a).toLowerCase();
            b = ((String) b).toLowerCase();
        }
        return Objects.equals(a, b);
    }

}

