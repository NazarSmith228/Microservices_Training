package com.epam.slsa.match;

import com.epam.slsa.dao.impl.LocationDaoImpl;
import com.epam.slsa.dto.criteria.CriteriaDto;
import com.epam.slsa.match.impl.FilterImpl;
import com.epam.slsa.match.impl.MatcherImpl;
import com.epam.slsa.model.*;
import org.assertj.core.util.Lists;
import org.assertj.core.util.Sets;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static com.epam.slsa.builders.criteria.CriteriaBuilder.getCriteria;
import static org.mockito.Mockito.when;

@SpringBootTest
public class MatcherImplTest {

    @InjectMocks
    private MatcherImpl matcher;

    @Spy
    private FilterImpl filter;

    @Mock
    private LocationDaoImpl locationDao;

    private CriteriaDto criteria;
    private List<Location> locations;

    @BeforeEach
    public void setUp() {
        Location location1 = Location.builder()
                .sportTypes(Sets.newHashSet(Lists.newArrayList(SportType.builder().id(2).name("Swimming").build())))
                .locationType(LocationType.builder()
                        .placing(Placing.INDOOR)
                        .build())
                .address(Address.builder()
                        .latitude(15.6)
                        .longitude(112.9)
                        .build())
                .build();

        Location location2 = Location.builder()
                .sportTypes(Sets.newHashSet(Lists.newArrayList(SportType.builder().id(1).name("Running").build())))
                .locationType(LocationType.builder()
                        .placing(Placing.INDOOR)
                        .build())
                .address(Address.builder()
                        .latitude(15.6)
                        .longitude(112.9)
                        .build())
                .build();

        Location location3 = Location.builder()
                .sportTypes(Sets.newHashSet(Lists.newArrayList(SportType.builder().id(2).name("Swimming").build())))
                .locationType(LocationType.builder()
                        .placing(Placing.ANY)
                        .build())
                .address(Address.builder()
                        .latitude(15.6)
                        .longitude(112.9)
                        .build())
                .build();

        Location location4 = Location.builder()
                .sportTypes(Sets.newHashSet(Lists.newArrayList(SportType.builder().id(2).name("Swimming").build())))
                .locationType(LocationType.builder()
                        .placing(Placing.INDOOR)
                        .build())
                .address(Address.builder()
                        .latitude(17.6)
                        .longitude(112.9)
                        .build())
                .build();

        criteria = getCriteria();
        locations = new ArrayList<>();
        locations.add(location1);
        locations.add(location2);
        locations.add(location3);
        locations.add(location4);
        when(locationDao.getAll()).thenReturn(locations);
    }

    @Test
    public void getSuitableVenueTest() {
        List<Location> venues = matcher.getSuitableVenue(criteria);
        Assertions.assertEquals("[Location(id=0, name=null, address=Address(id=0, latitude=15.6, longitude=112.9), " +
                        "locationType=LocationType(id=0, name=null, placing=INDOOR), webSite=null, phoneNumber=null, adminId=null, locationSchedule=null, " +
                        "coaches=null, photos=null, sportTypes=[SportType(id=2, name=Swimming)]), Location(id=0, name=null," +
                        " address=Address(id=0, latitude=15.6, longitude=112.9), locationType=LocationType(id=0, " +
                        "name=null, placing=ANY), webSite=null, phoneNumber=null, adminId=null, locationSchedule=null, coaches=null, photos=null, sportTypes=[SportType(id=2, " +
                        "name=Swimming)])]", venues.toString());
    }

}
