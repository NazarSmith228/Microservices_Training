package com.epam.spsa.service;

import com.epam.spsa.controller.builder.SportTypeDtoBuilder;
import com.epam.spsa.controller.builder.UserBuilder;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dao.UserStatsDao;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.dto.user.CommonUserStatDto;
import com.epam.spsa.dto.user.MainCommonUserStatDto;
import com.epam.spsa.dto.user.MainUserStatsDto;
import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.error.exception.DurationDateException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import com.epam.spsa.model.*;
import com.epam.spsa.service.impl.UserStatsServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
class UserStatsServiceTest {

    @InjectMocks
    private UserStatsServiceImpl userStatsService;

    @Mock
    UserStatsDao statsDao;

    @Mock
    UserDao userDao;

    @Mock
    private ModelMapper mapper;

    @Mock
    private SlsaClient slsaClient;

    @Test
    public void saveUserStatsCorrectTest() {
        UserStats stats = UserBuilder.getUserStats();
        UserStatsDto statsDto = UserBuilder.getUserStatsDto();
        MainUserStatsDto mainStatsDto = UserBuilder.getMainUserStatsDto();

        Mockito.when(userDao.getById(1)).thenReturn(getUser(1));

        Mockito.when(statsDao.save(stats)).thenReturn(stats);
        Mockito.when(statsDao.getLastStats(1)).thenThrow(new EmptyResultDataAccessException(1));

        Mockito.when(slsaClient.getById(1)).thenReturn(
                MainLocationDto.builder()
                        .coaches(Arrays.asList(MainCoachDto.builder()
                                .id(1)
                                .build()))
                        .sportTypes(getSportTypeSet())
                        .build());
        Mockito.when(mapper.map(Mockito.any(UserStats.class), Mockito.eq(MainUserStatsDto.class))).thenReturn(mainStatsDto);
        Mockito.when(mapper.map(Mockito.any(UserStatsDto.class), Mockito.eq(UserStats.class))).thenReturn(stats);
        Mockito.when(mapper.map(Mockito.any(SportTypeDto.class), Mockito.eq((SportType.class)))).thenReturn(SportTypeDtoBuilder.getSportType());

        MainUserStatsDto test = userStatsService.saveUserStats(statsDto, 1);

        assertNotNull(test);
        assertEquals(1, test.getUserId());
        assertEquals(0.0, test.getResultKm());
    }

    @Test
    public void saveUserStatsIncorrectTest() {
        UserStats stats = UserBuilder.getUserStats();
        UserStatsDto statsDto = UserBuilder.getUserStatsDto();

        Mockito.when(statsDao.save(stats)).thenReturn(stats);
        Mockito.when(statsDao.getLastStats(1)).thenReturn(UserStats.builder().insertionDate(LocalDate.parse("3030-01-01")).build());

        assertThrows(EntityNotFoundException.class, () -> userStatsService.saveUserStats(statsDto, 1));
    }

    @Test
    public void getAllUserStatsTest() {
        UserStats stats = UserBuilder.getUserStats();
        MainUserStatsDto mainStatsDto = UserBuilder.getMainUserStatsDto();

        Mockito.when(userDao.getById(1)).thenReturn(getUser(1));

        Mockito.when(slsaClient.getById(1)).thenReturn(
                MainLocationDto.builder()
                        .coaches(Arrays.asList(MainCoachDto.builder()
                                .id(1)
                                .build()))
                        .build());

        Mockito.when(statsDao.getAllByUserId(1)).thenReturn(Arrays.asList(stats));
        Mockito.when(mapper.map(Mockito.any(UserStats.class), Mockito.eq(MainUserStatsDto.class))).thenReturn(mainStatsDto);

        List<MainUserStatsDto> test = userStatsService.getAllUserStats(1);

        assertNotNull(test);
        assertEquals(1, test.size());
        assertEquals(1, test.get(0).getUserId());
        assertEquals(0.0, test.get(0).getResultKm());
    }

    @Test
    public void getLastUserStatsCorrectTest() {
        UserStats stats = UserBuilder.getUserStats();
        MainUserStatsDto mainStatsDto = UserBuilder.getMainUserStatsDto();

        Mockito.when(userDao.getById(1)).thenReturn(getUser(1));

        Mockito.when(statsDao.getLastStats(1)).thenReturn(stats);
        Mockito.when(mapper.map(Mockito.any(UserStats.class), Mockito.eq(MainUserStatsDto.class))).thenReturn(mainStatsDto);

        MainUserStatsDto test = userStatsService.getLastUserStats(1);

        assertNotNull(test);
        assertEquals(mainStatsDto, test);
    }

    @Test
    public void updateLastUserStatsTest() {
        UserStats stats = UserBuilder.getUserStats();
        UserStatsDto statsDto = UserBuilder.getUserStatsDto();
        MainUserStatsDto mainStatsDto = UserBuilder.getMainUserStatsDto();

        Mockito.when(userDao.getById(1)).thenReturn(getUser(1));

        Mockito.when(slsaClient.getById(1)).thenReturn(
                MainLocationDto.builder()
                        .coaches(Arrays.asList(MainCoachDto.builder()
                                .id(1)
                                .build()))
                        .sportTypes(getSportTypeSet())
                        .build());

        Mockito.when(statsDao.updateStats(stats)).thenReturn(stats);
        Mockito.when(mapper.map(Mockito.any(UserStatsDto.class), Mockito.eq(UserStats.class))).thenReturn(stats);
        Mockito.when(mapper.map(Mockito.any(UserStats.class), Mockito.eq(MainUserStatsDto.class))).thenReturn(mainStatsDto);

        MainUserStatsDto test = userStatsService.updateLastUserStats(statsDto, 1);

        assertNotNull(test);
        assertEquals(mainStatsDto, test);
    }

    @Test
    public void deleteUserStatsTest() {
        UserStats stats = UserBuilder.getUserStats();
        MainUserStatsDto mainStatsDto = UserBuilder.getMainUserStatsDto();

        Mockito.when(userDao.getById(1)).thenReturn(getUser(1));

        Mockito.when(mapper.map(Mockito.any(UserStatsDto.class), Mockito.eq(UserStats.class))).thenReturn(stats);
        Mockito.when(mapper.map(Mockito.any(UserStats.class), Mockito.eq(MainUserStatsDto.class))).thenReturn(mainStatsDto);
    }

    @Test
    public void getCommonStatTest() {
        int userId = 1;
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(SportTypeDto.builder()
                .id(1)
                .name("Swimming")
                .build());
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        CommonUserStatDto statDto = CommonUserStatDto.builder()
                .endOfInterval(LocalDate.parse("2020-02-12"))
                .startOfInterval(LocalDate.parse("2020-01-21"))
                .sportTypes(sportTypes)
                .locations(ids)
                .coaches(ids)
                .build();

        Mockito.when(userDao.getById(userId))
                .thenAnswer((Answer<User>) invocationOnMock -> getUser(invocationOnMock.getArgument(0)));

        Mockito.when(statsDao.getAllByUserId(userId))
                .thenAnswer((Answer<List<UserStats>>) invocationOnMock -> {
                    List<UserStats> list = new ArrayList<>();
                    list.add(UserStats.builder()
                            .id(1)
                            .coachId(1)
                            .locationId(1)
                            .insertionDate(LocalDate.parse("2020-02-10"))
                            .sportType(SportType.builder()
                                    .id(1)
                                    .name("Swimming")
                                    .build())
                            .resultKm(255)
                            .resultH(Duration.parse("PT2H"))
                            .build());
                    return list;
                });

        Mockito.when(mapper.map(Mockito.any(SportType.class), Mockito.eq(SportTypeDto.class)))
                .thenReturn(SportTypeDto.builder()
                        .id(1)
                        .name("Swimming")
                        .build());

        MainCommonUserStatDto mainCommonUserStatDto = userStatsService.getCommonStat(statDto, userId);
        Assertions.assertEquals(mainCommonUserStatDto.getLocations().size(), 1);
        Assertions.assertEquals(mainCommonUserStatDto.getResultKm(), 255);
    }

    @Test
    public void getCommonStatIncorrectTest() {
        int userId = 1;
        Set<SportTypeDto> sportTypes = new HashSet<>();
        sportTypes.add(SportTypeDto.builder()
                .id(1)
                .name("Swimming")
                .build());
        Set<Integer> ids = new HashSet<>();
        ids.add(1);
        CommonUserStatDto statDto = CommonUserStatDto.builder()
                .endOfInterval(LocalDate.parse("2020-02-12"))
                .startOfInterval(LocalDate.parse("2020-03-21"))
                .sportTypes(sportTypes)
                .locations(ids)
                .coaches(ids)
                .build();

        Assertions.assertThrows(DurationDateException.class, () -> userStatsService.getCommonStat(statDto, userId));
    }

    private User getUser(int id) {
        Set<Role> roles = new HashSet<>();
        roles.add(Role.builder().id(1).name("USER").build());
        return User.builder()
                .id(id)
                .name("Maksym")
                .surname("Natural")
                .dateOfBirth(LocalDate.of(2000, 5, 30))
                .email("210372@ukr.net")
                .address(Address.builder()
                        .latitude(42.14)
                        .longitude(75.2)
                        .build())
                .gender(Gender.MALE)
                .hasChildren(false)
                .phoneNumber("0980265499")
                .roles(roles)
                .build();
    }

    private Set<SportTypeDto> getSportTypeSet() {
        return new HashSet<>(Arrays.asList(SportTypeDtoBuilder.getSportTypeDto()));
    }
}