package com.epam.slsa.match;

import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.match.impl.CoachCriteriaFilterImpl;
import com.epam.slsa.match.impl.CoachMatcherImpl;
import com.epam.slsa.model.Coach;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
public class CoachMatcherImplTest {

    @InjectMocks
    private CoachMatcherImpl matcher;

    @Mock
    private CoachCriteriaFilterImpl filter;

    @Mock
    private PartnerClient partnerClient;

    private CoachCriteriaDto criteria;
    private List<Coach> coaches;

    @BeforeEach
    public void setUp() {

        criteria = CoachCriteriaDto.builder()
                .rating(5.00)
                .sportType("Football")
                .gender("female")
                .workWithChildren(false)
                .locationName("YogaMaster")
                .userId(1)
                .build();

        Mockito.when(partnerClient.getUserById(anyInt())).thenReturn(getMainUser(1));
        Mockito.when(filter.getFilteredCoachesByGender(getCoaches(),criteria)).thenReturn(Lists.newArrayList(getCoaches()));
        Mockito.when(filter.getFilteredCoachesByLocation(getCoaches(),criteria,getMainUser(1))).thenReturn(Lists.newArrayList(getCoaches()));
        Mockito.when(filter.getFilteredCoachesByRating(getCoaches(),criteria)).thenReturn(Lists.newArrayList(getCoaches()));
        Mockito.when(filter.getFilteredCoachesBySportType(getCoaches(),criteria)).thenReturn(Lists.newArrayList(getCoaches()));
        Mockito.when(filter.getFilteredCoachesByWorkWithChildren(getCoaches(),criteria)).thenReturn(Lists.newArrayList(getCoaches()));
    }

    @Test
    public void getSuitableCoachTest(){

        List<CriteriaCoachDto> coaches=matcher.getSuitableCoach(Lists.newArrayList(getCoaches()),criteria);
        Assertions.assertEquals(coaches.toString(),
                "[CriteriaCoachDto(id=2, rating=5.0, workWithChildren=false, userId=6, location=MainLocationDto(id=0, name=YogaMaster, " +
                        "address=AddressDto(latitude=49.843813, longitude=24.035848), adminId=null, locationType=null, " +
                        "webSite=null, phoneNumber=null, locationSchedule=null, coaches=null, photos=null, sportTypes=null), " +
                        "user=MainUserDto(id=1, name=null, surname=null, email=null, password=null, phoneNumber=null, photo=null, " +
                        "dateOfBirth=null, address=AddressDto(latitude=49.843813, longitude=24.035848), gender=female," +
                        " hasChildren=false, creationDate=null, roles=null)," +
                        " sportTypes=[SportTypeDto(id=3, name=Football)], comments=[], links=[])]");

    }

    private List<CriteriaCoachDto> getCoaches() {

        MainLocationDto location = MainLocationDto.builder()
                .name("YogaMaster")
                .address(AddressDto.builder()
                        .latitude(49.843813)
                        .longitude(24.035848)
                        .build())
                .build();

        CriteriaCoachDto coach = CriteriaCoachDto.builder()
                .id(2)
                .rating(5.0)
                .workWithChildren(false)
                .userId(6)
                .user(getMainUser(1))
                .sportTypes(getSportType())
                .location(location)
                .links(new HashSet<>())
                .comments(new HashSet<>())
                .build();

        return new ArrayList<>(Arrays.asList(coach));

    }

    private HashSet<SportTypeDto> getSportType() {

        SportTypeDto sportType = SportTypeDto.builder()
                .id(3)
                .name("Football")
                .build();
        SportTypeDto[] sportTypes = {sportType};
        return new HashSet<>(Arrays.asList(sportTypes));

    }

    private MainUserDto getMainUser(int id) {

        AddressDto userAddress = AddressDto.builder()
                .latitude(49.843813)
                .longitude(24.035848)
                .build();

        return MainUserDto.builder()
                .id(id)
                .gender("female")
                .address(userAddress)
                .build();
    }
}
