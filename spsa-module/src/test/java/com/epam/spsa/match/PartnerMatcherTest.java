package com.epam.spsa.match;

import com.epam.spsa.dao.CriteriaDao;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.match.impl.MatcherImpl;
import com.epam.spsa.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class PartnerMatcherTest {

    private Matcher match;

    @Mock
    private ModelMapper modelMapper;

    @Autowired
    private CriteriaFilter criteriaFilter;

    @BeforeEach
    public void setUp() {
        CriteriaDao criteriaDao = Mockito.mock(CriteriaDao.class);
        match = new MatcherImpl(criteriaDao, criteriaFilter);
        when(criteriaDao.getAll()).thenReturn(getCriteria());

        when(modelMapper.map(getUsers().get(0), CriteriaUserDto.class)).thenReturn(getCriteriaUserDto().get(0));
        when(modelMapper.map(getUsers().get(1), CriteriaUserDto.class)).thenReturn(getCriteriaUserDto().get(1));
        when(modelMapper.map(getUsers().get(2), CriteriaUserDto.class)).thenReturn(getCriteriaUserDto().get(2));
        when(modelMapper.map(getUsers().get(3), CriteriaUserDto.class)).thenReturn(getCriteriaUserDto().get(3));

        when(modelMapper.map(getCriteria().get(0), CriteriaDto.class)).thenReturn(getCriteriaDto().get(0));
        when(modelMapper.map(getCriteria().get(1), CriteriaDto.class)).thenReturn(getCriteriaDto().get(1));
        when(modelMapper.map(getCriteria().get(2), CriteriaDto.class)).thenReturn(getCriteriaDto().get(2));
        when(modelMapper.map(getCriteria().get(3), CriteriaDto.class)).thenReturn(getCriteriaDto().get(3));
        when(modelMapper.map(getCriteria().get(4), CriteriaDto.class)).thenReturn(getCriteriaDto().get(4));
        when(modelMapper.map(getCriteria().get(5), CriteriaDto.class)).thenReturn(getCriteriaDto().get(5));
        when(modelMapper.map(getCriteria().get(6), CriteriaDto.class)).thenReturn(getCriteriaDto().get(6));
        when(modelMapper.map(getCriteria().get(7), CriteriaDto.class)).thenReturn(getCriteriaDto().get(7));
        when(modelMapper.map(getCriteria().get(8), CriteriaDto.class)).thenReturn(getCriteriaDto().get(8));

    }

    @Test
    public void firstGenderTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(0), pageNumber, pageSize);
        List<CriteriaUserDto> answers = new ArrayList<>(Collections.singletonList(getCriteriaUserDto().get(2)));
        assertTrue(answers.containsAll(userList));
    }

    @Test
    public void secondGenderTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(2), pageNumber, pageSize);
        List<CriteriaUserDto> answers = new ArrayList<>(Collections.singletonList(getCriteriaUserDto().get(0)));
        assertTrue(answers.containsAll(userList));
    }

    //>20km
    @Test
    public void firstLocationTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(3), pageNumber, pageSize);
        assertTrue(userList.isEmpty());
    }

    //<20km
    @Test
    public void secondLocationTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(0), pageNumber, pageSize);
        List<CriteriaUserDto> answers = new ArrayList<>(Collections.singletonList(getCriteriaUserDto().get(2)));
        assertTrue(answers.containsAll(userList));
    }

    @Test
    public void maturityTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(7), pageNumber, pageSize);
        List<CriteriaUserDto> answers = new ArrayList<>(Collections.singletonList(getCriteriaUserDto().get(1)));
        assertTrue(answers.containsAll(userList));
    }

    @Test
    public void sportTypeTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(6), pageNumber, pageSize);
        List<CriteriaUserDto> answers = new ArrayList<>(Collections.singletonList(getCriteriaUserDto().get(2)));
        assertTrue(answers.containsAll(userList));
    }

    @Test
    public void dayTimeTest() {
        int pageNumber = 1;
        int pageSize = 3;
        List<CriteriaUserDto> userList = match.getSuitablePartner(getCriteria().get(6), pageNumber, pageSize);
        List<CriteriaUserDto> answers = new ArrayList<>(Collections.singletonList(getCriteriaUserDto().get(3)));
        assertTrue(answers.containsAll(userList));
    }

    private List<Criteria> getCriteria() {
        Criteria criteria1 = new Criteria(1, getUsers().get(0), getSportTypes().get(0), Maturity.PRO, 6, Gender.MALE, ActivityTime.EVENING);
        Criteria criteria2 = new Criteria(2, getUsers().get(1), getSportTypes().get(2), Maturity.BEGINNER, 1, Gender.MALE, ActivityTime.NOON);
        Criteria criteria3 = new Criteria(3, getUsers().get(2), getSportTypes().get(0), Maturity.PRO, 6, Gender.BOTH, ActivityTime.EVENING);
        Criteria criteria4 = new Criteria(4, getUsers().get(3), getSportTypes().get(2), Maturity.PRO, 0, Gender.MALE, ActivityTime.NOON);
        Criteria criteria5 = new Criteria(5, getUsers().get(2), getSportTypes().get(0), Maturity.PRO, 12, Gender.BOTH, ActivityTime.EVENING);
        Criteria criteria6 = new Criteria(6, getUsers().get(1), getSportTypes().get(2), Maturity.PRO, 3, Gender.MALE, ActivityTime.NOON);
        Criteria criteria7 = new Criteria(7, getUsers().get(3), getSportTypes().get(0), Maturity.PRO, 12, Gender.BOTH, ActivityTime.EVENING);
        Criteria criteria8 = new Criteria(8, getUsers().get(2), getSportTypes().get(2), Maturity.BEGINNER, 0, Gender.MALE, ActivityTime.NOON);
        Criteria criteria9 = new Criteria(9, getUsers().get(1), getSportTypes().get(2), Maturity.PRO, 0, Gender.MALE, ActivityTime.NOON);

        return new ArrayList<>(Arrays.asList(criteria1, criteria2, criteria3, criteria4, criteria5, criteria6, criteria7, criteria8, criteria9));
    }

    private List<CriteriaDto> getCriteriaDto() {
        CriteriaDto criteria1 = new CriteriaDto(getSportTypeDtos().get(0), Maturity.PRO.getMaturity(), 6, Gender.MALE.getGender(), ActivityTime.EVENING.getDayPart());
        CriteriaDto criteria2 = new CriteriaDto(getSportTypeDtos().get(2), Maturity.BEGINNER.getMaturity(), 1, Gender.MALE.getGender(), ActivityTime.NOON.getDayPart());
        CriteriaDto criteria3 = new CriteriaDto(getSportTypeDtos().get(0), Maturity.PRO.getMaturity(), 6, Gender.BOTH.getGender(), ActivityTime.EVENING.getDayPart());
        CriteriaDto criteria4 = new CriteriaDto(getSportTypeDtos().get(2), Maturity.PRO.getMaturity(), 0, Gender.MALE.getGender(), ActivityTime.NOON.getDayPart());
        CriteriaDto criteria5 = new CriteriaDto(getSportTypeDtos().get(0), Maturity.PRO.getMaturity(), 12, Gender.BOTH.getGender(), ActivityTime.EVENING.getDayPart());
        CriteriaDto criteria6 = new CriteriaDto(getSportTypeDtos().get(2), Maturity.PRO.getMaturity(), 3, Gender.MALE.getGender(), ActivityTime.NOON.getDayPart());
        CriteriaDto criteria7 = new CriteriaDto(getSportTypeDtos().get(0), Maturity.PRO.getMaturity(), 12, Gender.BOTH.getGender(), ActivityTime.EVENING.getDayPart());
        CriteriaDto criteria8 = new CriteriaDto(getSportTypeDtos().get(2), Maturity.BEGINNER.getMaturity(), 0, Gender.MALE.getGender(), ActivityTime.NOON.getDayPart());
        CriteriaDto criteria9 = new CriteriaDto(getSportTypeDtos().get(2), Maturity.PRO.getMaturity(), 0, Gender.MALE.getGender(), ActivityTime.NOON.getDayPart());

        return new ArrayList<>(Arrays.asList(criteria1, criteria2, criteria3, criteria4, criteria5, criteria6, criteria7, criteria8, criteria9));
    }

    private List<User> getUsers() {
        Address address1 = new Address();
        address1.setId(1);
        address1.setLatitude(49.8386268);
        address1.setLongitude(24.0353303);
        Address address2 = new Address();
        address2.setId(2);
        address2.setLatitude(49.8025141);
        address2.setLongitude(24.0088487);
        Address address3 = new Address();
        address3.setId(3);
        address3.setLatitude(49.8505141);
        address3.setLongitude(24.0238487);
        Address address4 = new Address();
        address4.setId(4);
        address4.setLatitude(149.8386268);
        address4.setLongitude(24.0353303);

        User user1 = User.builder()
                .id(1)
                .name("Ivan")
                .surname("Petrov")
                .password("1nadfj1F")
                .email("ds@fsg")
                .phoneNumber("3332242")
                .address(null)
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .address(address1)
                .gender(Gender.MALE)
                .hasChildren(true)
                .build();

        User user2 = User.builder()
                .id(2)
                .name("Petro")
                .surname("Petrov2")
                .password("1nadfj1F")
                .email("dhgs@fsg")
                .phoneNumber("3324223")
                .address(null)
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .address(address2)
                .gender(Gender.MALE)
                .hasChildren(true)
                .build();

        User user3 = User.builder()
                .id(3)
                .name("Yura")
                .surname("Petrov3")
                .password("1nadfj1Ffg")
                .email("ds@ffgsg")
                .phoneNumber("3324233")
                .address(null)
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .address(address3)
                .gender(Gender.MALE)
                .hasChildren(true)
                .build();

        User user4 = User.builder()
                .id(4)
                .name("Nazar")
                .surname("Petro4v")
                .password("1nadfj1Fh")
                .email("ds@fsghj")
                .phoneNumber("332423")
                .address(null)
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .address(address4)
                .gender(Gender.MALE)
                .hasChildren(true)
                .build();

        return new ArrayList<>(Arrays.asList(user1, user2, user3, user4));
    }

    private List<CriteriaUserDto> getCriteriaUserDto(){
        CriteriaUserDto criteriaUserDto1 = CriteriaUserDto.builder()
                .id(1)
                .name("Ivan")
                .email("ds@fsg")
                .city(null)
                .gender(Gender.MALE)
                .criteriaDto(getCriteriaDto().get(0))
                .build();
        CriteriaUserDto criteriaUserDto2 = CriteriaUserDto.builder()
                .id(2)
                .name("Petro")
                .email("dhgs@fsg")
                .city(null)
                .gender(Gender.MALE)
                .criteriaDto(getCriteriaDto().get(1))
                .build();
        CriteriaUserDto criteriaUserDto3 = CriteriaUserDto.builder()
                .id(3)
                .name("Yura")
                .email("ds@ffgsg")
                .city(null)
                .gender(Gender.MALE)
                .criteriaDto(getCriteriaDto().get(2))
                .build();
        CriteriaUserDto criteriaUserDto4 = CriteriaUserDto.builder()
                .id(4)
                .name("Nazar")
                .email("ds@fsghj")
                .city(null)
                .gender(Gender.MALE)
                .criteriaDto(getCriteriaDto().get(3))
                .build();
        return new ArrayList<>(Arrays.asList(criteriaUserDto1, criteriaUserDto2, criteriaUserDto3, criteriaUserDto4));
    }

    private List<SportType> getSportTypes() {
        SportType sportType1 = new SportType();
        sportType1.setId(1);
        sportType1.setName("Running");
        SportType sportType2 = new SportType();
        sportType2.setId(2);
        sportType2.setName("Swimming");
        SportType sportType3 = new SportType();
        sportType3.setId(3);
        sportType3.setName("Football");
        SportType sportType4 = new SportType();
        sportType4.setId(1);
        sportType4.setName("Yoga");
        return new ArrayList<>(Arrays.asList(sportType1, sportType2, sportType3, sportType4));
    }

    private List<SportTypeDto> getSportTypeDtos() {
        SportTypeDto sportType1 = SportTypeDto.builder().id(1).name("Running").build();
        SportTypeDto sportType2 = SportTypeDto.builder().id(2).name("Swimming").build();
        SportTypeDto sportType3 = SportTypeDto.builder().id(3).name("Football").build();
        SportTypeDto sportType4 = SportTypeDto.builder().id(4).name("Yoga").build();
        return new ArrayList<>(Arrays.asList(sportType1, sportType2, sportType3, sportType4));
    }

}