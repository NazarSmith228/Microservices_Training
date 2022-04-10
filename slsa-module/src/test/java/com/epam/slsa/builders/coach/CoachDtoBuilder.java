package com.epam.slsa.builders.coach;

import com.epam.slsa.builders.location.LocationDtoBuilder;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coach.MainCoachDto;
import com.epam.slsa.dto.link.LinkDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.model.*;

import java.util.HashSet;
import java.util.Set;

import static com.epam.slsa.builders.coach.CoachInfo.*;

public class CoachDtoBuilder {

    public static CoachDto getCoachDto() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(
                SportTypeDto.builder()
                        .id(1)
                        .name("Running")
                        .build()
        );

        Set<LinkDto> links = new HashSet<>();
        links.add(LinkDto.builder()
                .url("http://test")
                .type("youtube")
                .build()
        );

        return CoachDto.builder()
                .userId(1)
                .rating(rating)
                .workWithChildren(workWithChildren)
                .sportTypes(sportTypes)
                .links(links)
                .build();
    }

    public static MainCoachDto getMainCoachDto() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(
                SportTypeDto.builder()
                        .id(1)
                        .name("Running")
                        .build()
        );

        return MainCoachDto.builder()
                .userId(1)
                .rating(rating)
                .workWithChildren(workWithChildren)
                .sportTypes(sportTypes)
                .id(locationId)
                .build();
    }

    public static CriteriaCoachDto getCriteriaCoachDto() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(
                SportTypeDto.builder()
                        .id(1)
                        .name("Running")
                        .build()
        );

        return CriteriaCoachDto.builder()
                .userId(1)
                .rating(rating)
                .workWithChildren(workWithChildren)
                .sportTypes(sportTypes)
                .id(locationId)
                .build();
    }

    public static Coach getCoach() {
        Set<SportType> sportTypes = new HashSet<>();
        sportTypes.add(
                SportType.builder()
                        .id(1)
                        .name("Running")
                        .build()
        );

        Set<Link> links = new HashSet<>();
        links.add(Link.builder()
                .id(0)
                .url("http://test")
                .type(LinkType.YOUTUBE)
                .build()
        );

        return Coach.builder()
                .location(LocationDtoBuilder.getLocation())
                .userId(1)
                .rating(rating)
                .workWithChildren(workWithChildren)
                .sportTypes(sportTypes)
                .links(links)
                .build();
    }

}
