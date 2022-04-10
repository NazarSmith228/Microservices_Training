package com.epam.slsa.service;

import com.epam.slsa.builders.criteria.CriteriaBuilder;
import com.epam.slsa.builders.location.LocationDtoBuilder;
import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.location.CriteriaLocationDto;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.match.Matcher;
import com.epam.slsa.model.Location;
import com.epam.slsa.service.impl.VenueServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.epam.slsa.builders.criteria.CriteriaBuilder.getCriteria;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(SpringExtension.class)
public class VenueServiceImplTest {

    @InjectMocks
    VenueServiceImpl venueService;

    @Mock
    private Matcher matcher;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PartnerClient partnerClient;

    private CriteriaDto criteriaDto;

    @BeforeEach
    public void setUpMockito() {

        Mockito.when(modelMapper.map(any(CriteriaDto.class), Mockito.eq(CriteriaDto.class)))
                .thenReturn(CriteriaBuilder.getCriteriaDto());
        Mockito.when(modelMapper.map(any(CriteriaDto.class), Mockito.eq(CriteriaDto.class)))
                .thenReturn(getCriteria());
        Mockito.when(modelMapper.map(any(Location.class), Mockito.eq(CriteriaLocationDto.class)))
                .thenReturn(LocationDtoBuilder.getCriteriaLocationDto());

        Mockito.when(matcher.getSuitableVenue(any(CriteriaDto.class)))
                .thenReturn(Lists.newArrayList(LocationDtoBuilder.getLocation()));
        Mockito.doNothing().when(modelMapper).map(any(CriteriaDto.class), any(CriteriaDto.class));
    }

    @BeforeEach
    public void setUpVariables() {
        criteriaDto = CriteriaBuilder.getCriteriaDto();
    }

    @Test
    public void getSuitableLocation() {
        List<CriteriaLocationDto> mainLocationDtos = venueService.getSuitableLocation(CriteriaBuilder.getCriteriaDto());
        Assertions.assertEquals("[CriteriaLocationDto(id=5, name=Fun club, address=AddressDto(latitude=12.5, longitude=120.0)," +
                " adminId=null, locationType=LocationTypeDto(id=4, name=Studio, placing=Indoor), webSite=null, phoneNumber=null," +
                " locationSchedule=null, coaches=[CriteriaCoachDto(id=5, rating=5.0, workWithChildren=true, userId=1, location=null, " +
                "user=null, sportTypes=[SportTypeDto(id=1, name=Running)], comments=null, links=null)], photos=null, " +
                "sportTypes=[SportTypeDto(id=2, name=Football)])]", mainLocationDtos.toString());

    }

}