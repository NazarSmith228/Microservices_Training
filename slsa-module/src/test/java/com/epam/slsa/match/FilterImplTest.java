package com.epam.slsa.match;

import com.epam.slsa.builders.location.LocationInfo;
import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.match.impl.FilterImpl;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.Placing;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocation;
import static com.epam.slsa.builders.sportType.SportTypeBuilder.getSportTypeDto;

@SpringBootTest
public class FilterImplTest {

    @InjectMocks
    private FilterImpl filter;

    private CriteriaDto criteria;

    @BeforeEach
    public void setUp() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(getSportTypeDto());
        criteria = CriteriaDto.builder()
                .latitude(LocationInfo.latitude)
                .longitude(LocationInfo.longitude)
                .placing(Placing.OUTDOOR.getName())
                .sportTypes(sportTypes)
                .build();

    }

    @Test
    public void getFilteredLocationsBySportType() {
        List<Location> locations = filter.getFilteredLocationsBySportType(
                Lists.newArrayList(getLocation()), criteria);
        Assertions.assertEquals(locations.toString(),
                "[Location(id=2, name=Fun club, address=Address(id=0, latitude=120.0, longitude=12.5)," +
                        " locationType=LocationType(id=5, name=Studio, placing=OUTDOOR), webSite=null, phoneNumber=null, adminId=null, locationSchedule=null," +
                        " coaches=null, photos=null, " +
                        "sportTypes=[SportType(id=2, name=Swimming)])]");
    }

    @Test
    public void getFilteredLocationsByIncorrectSportType() {
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(SportTypeDto.builder().id(1).name("Running").build());
        criteria.setSportTypes(sportTypes);
        List<Location> locations = filter.getFilteredLocationsBySportType(
                Lists.newArrayList(getLocation()), criteria);
        Assertions.assertEquals("[]", locations.toString());

    }

    @Test
    public void getFilteredLocationsByPlacing() {
        List<Location> locations = filter.getFilteredLocationsByPlacing(
                Lists.newArrayList(getLocation()), Placing.getFromName(criteria.getPlacing()));
        Assertions.assertEquals("[Location(id=2, name=Fun club, address=Address(id=0, latitude=120.0, longitude=12.5)," +
                " locationType=LocationType(id=5, name=Studio, placing=OUTDOOR), webSite=null, phoneNumber=null, adminId=null, locationSchedule=null, " +
                "coaches=null, photos=null, sportTypes=[SportType(id=2, name=Swimming)])]", locations.toString());
    }

    @Test
    public void getFilteredLocationsByAnyPlacing() {
        criteria.setPlacing(Placing.ANY.getName());
        List<Location> locations = filter.getFilteredLocationsByPlacing(
                Lists.newArrayList(getLocation()), Placing.getFromName(criteria.getPlacing()));
        Assertions.assertEquals("[Location(id=2, name=Fun club, address=Address(id=0, latitude=120.0, longitude=12.5)," +
                " locationType=LocationType(id=5, name=Studio, placing=OUTDOOR), webSite=null, phoneNumber=null, adminId=null, locationSchedule=null, " +
                "coaches=null, photos=null, sportTypes=[SportType(id=2, name=Swimming)])]", locations.toString());
    }

    @Test
    public void getFilteredLocationsByIncorrectPlacing() {
        criteria.setPlacing(Placing.INDOOR.getName());
        List<Location> locations = filter.getFilteredLocationsByPlacing(
                Lists.newArrayList(getLocation()), Placing.getFromName(criteria.getPlacing()));
        Assertions.assertEquals("[]", locations.toString());
    }

    @Test
    public void getFilteredLocationsByAddress() {
        List<Location> locations = filter.getFilteredLocationsByAddress(
                Lists.newArrayList(getLocation()), criteria);
        Assertions.assertEquals(
                "[Location(id=2, name=Fun club, address=Address(id=0, latitude=120.0, longitude=12.5)," +
                        " locationType=LocationType(id=5, name=Studio, placing=OUTDOOR), webSite=null, phoneNumber=null, adminId=null, locationSchedule=null, " +
                        "coaches=null, photos=null, sportTypes=[SportType(id=2, name=Swimming)])]",
                locations.toString());
    }

}