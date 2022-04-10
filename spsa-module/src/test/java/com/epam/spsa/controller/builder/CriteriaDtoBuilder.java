package com.epam.spsa.controller.builder;

import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.model.ActivityTime;
import com.epam.spsa.model.Criteria;
import com.epam.spsa.model.Gender;
import com.epam.spsa.model.Maturity;

import static com.epam.spsa.controller.builder.SportTypeDtoBuilder.getSportType;
import static com.epam.spsa.controller.builder.SportTypeDtoBuilder.getSportTypeDto;

public class CriteriaDtoBuilder {

    public static CriteriaDto getCriteriaDto() {
        return CriteriaDto.builder()
                .activityTime("Morning")
                .gender("Male")
                .maturity("Pro")
                .runningDistance(20)
                .sportType(getSportTypeDto())
                .build();
    }

    public static Criteria getCriteria() {
        return Criteria.builder()
                .activityTime(ActivityTime.MORNING)
                .gender(Gender.MALE)
                .maturity(Maturity.PRO)
                .runningDistance(20)
                .sportType(getSportType())
                .build();
    }

    public static MainCriteriaDto getMainCriteriaDto1() {
        return MainCriteriaDto.builder()
                .id(1)
                .activityTime("Morning")
                .gender("Male")
                .maturity("Pro")
                .runningDistance(20)
                .sportType(getSportTypeDto())
                .build();
    }

    public static MainCriteriaDto getMainCriteriaDto2() {
        return MainCriteriaDto.builder()
                .id(2)
                .activityTime("Morning")
                .gender("Male")
                .maturity("Pro")
                .runningDistance(20)
                .sportType(getSportTypeDto())
                .build();
    }

}
