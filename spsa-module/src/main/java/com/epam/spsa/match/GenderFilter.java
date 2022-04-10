package com.epam.spsa.match;

import com.epam.spsa.model.Criteria;
import com.epam.spsa.model.Gender;

public class GenderFilter {

    public static boolean checkIfNotNull(Criteria otherCriteria) {
        return otherCriteria.getGender() != null;
    }

    public static boolean checkFirstGenderCase(String userGender, String genderFromCriteria, Criteria otherCriteria) {
        String otherUserGender = otherCriteria.getUser().getGender().getGender();
        String otherCriteriaGender = otherCriteria.getGender().getGender();

        return otherUserGender.equalsIgnoreCase(genderFromCriteria)
                && otherCriteriaGender.equalsIgnoreCase(userGender);
    }

    public static boolean checkSecondGenderCase(String userGender, String genderFromCriteria, Criteria otherCriteria) {
        String genderFromOtherCriteria = otherCriteria.getGender().getGender();

        return genderFromCriteria.equalsIgnoreCase(Gender.BOTH.getGender())
                && genderFromOtherCriteria.equalsIgnoreCase(userGender);
    }

    public static boolean checkThirdGenderCase(String genderFromCriteria, Criteria otherCriteria) {
        String otherUserGender = otherCriteria.getUser().getGender().getGender();
        String genderFromOtherCriteria = otherCriteria.getGender().getGender();

        return genderFromOtherCriteria.equalsIgnoreCase(Gender.BOTH.getGender())
                && genderFromCriteria.equalsIgnoreCase(otherUserGender);
    }

    public static boolean checkFourthGenderCase(String genderFromCriteria, Criteria otherCriteria) {
        String genderFromOtherCriteria = otherCriteria.getGender().getGender();

        return genderFromCriteria.equalsIgnoreCase(Gender.BOTH.getGender())
                && genderFromOtherCriteria.equalsIgnoreCase(Gender.BOTH.getGender());
    }

}
