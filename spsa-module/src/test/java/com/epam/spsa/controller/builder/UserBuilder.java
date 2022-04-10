package com.epam.spsa.controller.builder;

import com.epam.spsa.converter.StringLocalDateConverter;
import com.epam.spsa.dto.address.AddressDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.user.*;
import com.epam.spsa.model.Gender;
import com.epam.spsa.model.SportType;
import com.epam.spsa.model.User;
import com.epam.spsa.model.UserStats;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.dto.user.MainUserStatsDto;
import com.epam.spsa.dto.user.UserDto;
import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.model.*;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class UserBuilder extends UserInfo {

    private static AddressDto addressDto;
    private static Address address;
    private static SportType sportType;

    static {
        sportType = SportType.builder()
                .id(1)
                .name("Running")
                .build();

        addressDto = AddressDto.builder()
                .latitude(20)
                .longitude(20)
                .build();
        address = Address.builder()
                .id(5)
                .user(UserBuilder.getUser())
                .latitude(20)
                .longitude(20)
                .build();

    }

    public static UserDto getUserDto() {
        return UserDto.builder()
                .address(addressDto)
                .gender(UserInfo.gender)
                .dateOfBirth(UserInfo.dateOfBirth)
                .email(UserInfo.email)
                .hasChildren(UserInfo.hasChildren)
                .password(UserInfo.password)
                .name(UserInfo.name)
                .phoneNumber(UserInfo.phoneNumber)
                .surname(surname)
                .build();
    }

    public static MainUserDto getMainUserDto() {
        return MainUserDto.builder()
                .id(5)
                .gender(UserInfo.gender)
                .dateOfBirth(UserInfo.dateOfBirth)
                .email(UserInfo.email)
                .hasChildren(UserInfo.hasChildren)
                .name(UserInfo.name)
                .phoneNumber(UserInfo.phoneNumber)
                .surname(surname)
                .address(addressDto)
                .build();
    }
    public static User getUser() {
        return User.builder()
                .id(5)
                .gender(Gender.getFromName(UserInfo.gender))
                .dateOfBirth(LocalDate.of(2000, 5,30))
                .email(UserInfo.email)
                .hasChildren(UserInfo.hasChildren)
                .name(UserInfo.name)
                .phoneNumber(UserInfo.phoneNumber)
                .surname(surname)
                .address(address)
                .build();
    }

    public static CriteriaUserDto getCriteriaUserDto() {
        return CriteriaUserDto.builder()
                .id(5)
                .gender(Gender.getFromName(UserInfo.gender))
                .email(UserInfo.email)
                .name(UserInfo.name)
                .city("Lviv")
                .build();
    }

    public static UserStatsDto getUserStatsDto() {
        return UserStatsDto.builder()
                .sportType(SportTypeDtoBuilder.getSportTypeDto())
                .resultKm(0.0)
                .resultH("PT1H0M0S")
                .locationId(1)
                .coachId(1)
                .build();
    }

    public static MainUserStatsDto getMainUserStatsDto() {
        return MainUserStatsDto.builder()
                .userId(1)
                .sportType(SportTypeDtoBuilder.getSportTypeDto())
                .resultKm(0.0)
                .resultH("PT1H0M0S")
                .locationId(1)
                .coachId(1)
                .insertionDate(LocalDate.parse("2020-04-13"))
                .build();
    }

    public static UserStats getUserStats() {
        return UserStats.builder()
                .user(User.builder().id(1).name("Victor").surname("Pickuluk").build())
                .insertionDate(LocalDate.parse("2020-04-09"))
                .sportType(SportTypeDtoBuilder.getSportType())
                .resultKm(0.0)
                .resultH(Duration.parse("PT1H0M0S"))
                .locationId(1)
                .coachId(1)
                .build();
    }

    public static MainCommonUserStatDto getMainCommonStatDto() {
        Set<Integer> coaches = new HashSet<>();
        coaches.add(1);
        coaches.add(2);

        Set<Integer> locations = new HashSet<>();
        locations.add(1);
        coaches.add(2);

        return MainCommonUserStatDto.builder()
                .coaches(coaches)
                .locations(locations)
                .resultH("PT17H3M12S")
                .resultKm(25)
                .build();
    }

    public static CommonUserStatDto getCommonStatDto() {
        return CommonUserStatDto.builder()
                .endOfInterval(LocalDate.parse("2020-02-12"))
                .startOfInterval(LocalDate.parse("2020-04-21"))
                .build();
    }

}
