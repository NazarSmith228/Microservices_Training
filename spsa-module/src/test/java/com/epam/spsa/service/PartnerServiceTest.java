package com.epam.spsa.service;

import com.epam.spsa.controller.builder.CriteriaDtoBuilder;
import com.epam.spsa.dao.CriteriaDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.criteria.CriteriaDto;
import com.epam.spsa.dto.criteria.CriteriaUserDto;
import com.epam.spsa.dto.criteria.MainCriteriaDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.EntityNotFullException;
import com.epam.spsa.match.Matcher;
import com.epam.spsa.model.*;
import com.epam.spsa.service.impl.PartnerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PartnerServiceTest {

    @InjectMocks
    private PartnerServiceImpl partnerService;

    @Mock
    private Matcher matcher;

    @Mock
    private CriteriaDao criteriaDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private UserDao userDao;

    private CriteriaDto getCriteriaDto() {
        return CriteriaDto.builder()
                .activityTime("Morning")
                .gender("Male")
                .maturity("Pro")
                .runningDistance(15)
                .sportType(SportTypeDto.builder()
                        .id(1)
                        .name("Running")
                        .build())
                .build();
    }

    private Criteria getCriteria() {
        return Criteria.builder()
                .id(1)
                .activityTime(ActivityTime.MORNING)
                .gender(Gender.MALE)
                .maturity(Maturity.PRO)
                .runningDistance(15)
                .sportType(SportType.builder()
                        .id(1)
                        .name("Running")
                        .build())
                .build();
    }

    private User getUser() {
        return User.builder()
                .name("Nazar")
                .surname("Smith")
                .phoneNumber("0980254255")
                .hasChildren(false)
                .gender(Gender.MALE)
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .build();
    }

    @Test
    public void getSuitablePartnerTest() {
        int id = 1;
        int pageNumber = 1;
        int pageSize = 3;
        CriteriaDto criteriaDto = getCriteriaDto();

        when(modelMapper.map(criteriaDto, Criteria.class)).thenReturn(getCriteria());

        when(userDao.getById(id)).thenAnswer((Answer<User>) invocationOnMock -> {
            User user = getUser();
            user.setId(invocationOnMock.getArgument(0));
            return user;
        });
        User users = User.builder()
                .id(2)
                .address(Address.builder()
                        .id(2)
                        .longitude(12.151)
                        .latitude(45.552)
                        .build())
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .email("2103425@ukr.net")
                .gender(Gender.MALE)
                .hasChildren(false)
                .name("Max")
                .phoneNumber("0980265133")
                .surname("Korb")
                .build();

        CriteriaUserDto criteriaUserDto = CriteriaUserDto.builder()
                .id(2)
                .email("2103425@ukr.net")
                .gender(Gender.MALE)
                .name("Max")
                .build();

        when(matcher.getSuitablePartner(Mockito.any(Criteria.class), eq(pageNumber), eq(pageSize))).thenAnswer(
                (Answer<List<CriteriaUserDto>>) invocationOnMock -> Arrays.asList(criteriaUserDto));
        when(modelMapper.map(Mockito.any(User.class), eq(CriteriaUserDto.class))).thenAnswer(
                (Answer<CriteriaUserDto>) invocationOnMock -> {
                    User user = invocationOnMock.getArgument(0);
                    return CriteriaUserDto.builder()
                            .id(user.getId())
                            .email(user.getEmail())
                            .gender(user.getGender())
                            .name(user.getName())
                            .build();
                });
        when(userDao.getById(anyInt())).thenReturn(users);
        when(criteriaDao.getByUserId(anyInt())).thenReturn(Collections.singletonList(CriteriaDtoBuilder.getCriteria()));

        PaginationDto<CriteriaUserDto> allPartnersDto = partnerService.getSuitablePartner(criteriaDto, id, pageNumber, pageSize);

        assertEquals(criteriaDto.getGender(), allPartnersDto.getEntities().get(0).getGender().getGender());
    }

    @Test
    public void getSuitablePartnerIncorrectTest() {
        int id = 1;
        int pageNumber = 1;
        int pageSize = 3;
        CriteriaDto criteriaDto = getCriteriaDto();

        when(modelMapper.map(criteriaDto, Criteria.class)).thenReturn(getCriteria());

        when(userDao.getById(id)).thenAnswer((Answer<User>) invocationOnMock -> {
            User user = getUser();
            user.setId(invocationOnMock.getArgument(0));
            return user;
        });

        assertThrows(EntityNotFullException.class,
                () -> partnerService.getSuitablePartner(criteriaDto, id, pageNumber, pageSize));
    }

    @Test
    public void saveTest() {
        int id = 1;
        CriteriaDto criteriaDto = getCriteriaDto();

        when(modelMapper.map(criteriaDto, Criteria.class)).thenReturn(getCriteria());

        when(userDao.getById(id)).thenAnswer((Answer<User>) invocationOnMock -> {
            User user = getUser();
            user.setId(invocationOnMock.getArgument(0));
            return user;
        });

        when(criteriaDao.save(Mockito.any(Criteria.class))).thenAnswer(
                (Answer<Criteria>) invocationOnMock ->
                        invocationOnMock.getArgument(0)
        );

        when(modelMapper.map(Mockito.any(Criteria.class), eq(CriteriaDto.class)))
                .thenReturn(getCriteriaDto());

        int criteriaDto1Id = partnerService.save(criteriaDto, id);

        assertEquals(id, criteriaDto1Id);
    }

    @Test
    public void saveIncorrectTest() {
        int id = 1;
        CriteriaDto criteriaDto = getCriteriaDto();

        when(modelMapper.map(criteriaDto, Criteria.class)).thenReturn(getCriteria());

        when(userDao.getById(id)).thenAnswer((Answer<User>) invocationOnMock -> {
            User user = getUser();
            user.setId(invocationOnMock.getArgument(0));
            return user;
        });

        assertThrows(EntityNotFoundException.class, () -> {
            partnerService.save(criteriaDto, 666);
        });
    }

    @Test
    public void getAllTest() {
        Criteria criteria = getCriteria();
        criteria.setId(1);
        Criteria[] criteriaList = {criteria};

        when(criteriaDao.getAll()).thenReturn(Arrays.asList(criteriaList));

        when(modelMapper.map(criteria, MainCriteriaDto.class)).thenAnswer(
                (Answer<MainCriteriaDto>) invocationOnMock -> {
                    Criteria criteria1 = invocationOnMock.getArgument(0);
                    SportType sportType = criteria1.getSportType();
                    return MainCriteriaDto.builder()
                            .id(criteria1.getId())
                            .activityTime(criteria1.getActivityTime().getDayPart())
                            .gender(criteria1.getGender().getGender())
                            .maturity(criteria1.getMaturity().getMaturity())
                            .runningDistance(criteria1.getRunningDistance())
                            .sportType(SportTypeDto.builder()
                                    .id(sportType.getId())
                                    .name(sportType.getName())
                                    .build())
                            .build();
                });

        MainCriteriaDto mainCriteriaDto = partnerService.getAll().get(0);

        assertEquals(criteria.getId(), mainCriteriaDto.getId());
    }

    @Test
    public void getByUserIdTest() {
        int id = 1;

        Criteria criteria = getCriteria();
        criteria.setId(id);
        Criteria[] criteriaList = {criteria};

        User user = getUser();
        criteria.setUser(user);

        when(userDao.getById(id)).thenReturn(user);

        when(criteriaDao.getByUserId(id)).thenReturn(Arrays.asList(criteriaList));

        when(modelMapper.map(Mockito.any(Criteria.class), eq(CriteriaDto.class)))
                .thenReturn(getCriteriaDto());

        assertTrue(partnerService.getCriteriaByUserId(id).size() > 0);
    }

}