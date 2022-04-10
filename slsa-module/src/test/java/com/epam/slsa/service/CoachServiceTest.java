package com.epam.slsa.service;

import com.epam.slsa.dao.CoachDao;
import com.epam.slsa.dao.LinkDao;
import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coach.MainCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.dto.link.LinkDto;
import com.epam.slsa.dto.link.MainLinkDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.dto.sportType.SportTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.error.exception.EntityRoleException;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.feign.dto.RoleDto;
import com.epam.slsa.match.impl.CoachMatcherImpl;
import com.epam.slsa.model.*;
import com.epam.slsa.s3api.S3Manager;
import com.epam.slsa.service.impl.CoachServiceImpl;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.NoResultException;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
public class CoachServiceTest {

    @InjectMocks
    private CoachServiceImpl coachService;

    @Mock
    private CoachDao coachDao;

    @Mock
    private LocationDao locationDao;

    @Mock
    private LinkDao linkDao;

    @Mock
    private ModelMapper mapper;

    @Mock
    private PartnerClient partnerClient;

    @Mock
    private S3Manager manager;

    @Mock
    private CoachMatcherImpl matcher;

    @Test
    public void saveTest() {
        CoachDto coachDto = getCoachDto();
        int locationId = 1;

        Mockito.when(linkDao.getByUrl("http://test")).thenThrow(NoResultException.class);

        Mockito.when(mapper.map(Mockito.any(CoachDto.class), Mockito.eq(Coach.class)))
                .thenReturn(getCoach(0));

        Mockito.when(locationDao.getById(locationId)).thenAnswer(
                (Answer<Location>) invocationOnMock ->
                        getLocation(locationId)
        );

        Mockito.when(locationDao.getById(locationId)).
                thenAnswer((Answer<Location>) invocationOnMock ->
                        getLocation(invocationOnMock.getArgument(0))
                );

        Mockito.when(coachDao.save(Mockito.any(Coach.class)))
                .thenAnswer((Answer<Coach>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenReturn(getMainCoachDto(1));

        Mockito.when(partnerClient.getUserById(anyInt())).thenReturn(getMainUser(1));

        MainCoachDto coachDto1 = coachService.save(coachDto, locationId);

        Assertions.assertEquals(coachDto.getRating(), coachDto1.getRating());
    }

    @Test
    public void saveIncorrectTest() {
        CoachDto coachDto = getCoachDto();
        int locationId = 1;

        Mockito.when(mapper.map(coachDto, Coach.class)).thenReturn(getCoach(0));

        Mockito.when(linkDao.getByUrl("http://test")).thenThrow(NoResultException.class);

        Mockito.when(locationDao.getById(locationId)).thenAnswer(
                (Answer<Location>) invocationOnMock ->
                        getLocation(locationId)
        );

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> coachService.save(coachDto, 666));
    }

    @Test
    public void saveUserIsNotCoachTest() {
        int locationId = 1;
        MainUserDto userDto = getMainUser(1);
        CoachDto coachDto = getCoachDto();

        Set<RoleDto> roles = new HashSet<>();
        roles.add(RoleDto.builder().id(1).name("USER").build());
        userDto.setRoles(roles);

        Mockito.when(locationDao.getById(locationId)).thenReturn(getLocation(locationId));
        Mockito.when(partnerClient.getUserById(userDto.getId())).thenReturn(userDto);

        Assertions.assertThrows(EntityRoleException.class,
                () -> coachService.save(coachDto, locationId));
    }

    @Test
    public void updateTest() {
        CoachDto editedCoachDto = getCoachDto();
        editedCoachDto.setRating(1.2);
        int locationId = 1;
        int coachId = 1;

        Mockito.when(coachDao.getByIdAndLocationId(coachId, locationId))
                .thenAnswer((Answer<Coach>) invocationOnMock -> getCoach(invocationOnMock.getArgument(0)));

        Mockito.when(linkDao.getByUrl("http://test")).thenThrow(NoResultException.class);

        Mockito.when(mapper.map(Mockito.any(CoachDto.class), Mockito.eq(Coach.class)))
                .thenAnswer((Answer<Coach>) invocationOnMock -> getCoach(invocationOnMock.getArgument(0)));

        Mockito.when(coachDao.update(Mockito.any(Coach.class))).thenAnswer(
                (Answer<Coach>) invocationOnMock -> invocationOnMock.getArgument(0));

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenAnswer((Answer<MainCoachDto>) invocationOnMock -> {
                    MainCoachDto coachDto = getMainCoachDto(coachId);
                    coachDto.setRating(editedCoachDto.getRating());
                    return coachDto;
                });

        MainCoachDto coachDto = coachService.update(editedCoachDto, locationId, coachId);

        Assertions.assertEquals(editedCoachDto.getRating(), coachDto.getRating());
    }

    @Test
    public void deleteTest() {
        int id = 1;
        Mockito.when(coachDao.getById(id))
                .thenAnswer((Answer<Coach>) invocationOnMock -> getCoach(invocationOnMock.getArgument(0)));

        Mockito.doNothing().when(coachDao).delete(Mockito.any(Coach.class));

        Assertions.assertDoesNotThrow(() -> coachService.delete(id));
    }

    @Test
    public void deleteLinkTest() {
        int id = 1;
        Mockito.when(linkDao.getById(1)).thenReturn(Link.builder().type(LinkType.YOUTUBE).url("http").build());

        Mockito.doNothing().when(linkDao).delete(Mockito.any(Link.class));

        Assertions.assertDoesNotThrow(() -> coachService.deleteCoachLinkById(id));
    }

    @Test
    public void getByIdTest() {
        int id = 1;

        Mockito.when(coachDao.getById(id)).thenAnswer(
                (Answer<Coach>) invocationOnMock ->
                        getCoach(invocationOnMock.getArgument(0))
        );

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenAnswer((Answer<MainCoachDto>) invocationOnMock -> {
                    Coach coach = invocationOnMock.getArgument(0);
                    return getMainCoachDto(coach.getId());
                });

        MainCoachDto coachDto = coachService.getById(id);

        Assertions.assertEquals(id, coachDto.getId());
    }

    @Test
    public void getByUserIdTest() {
        int id = 1;

        Mockito.when(coachDao.getByUserId(id)).thenReturn(getCoach(id));

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenReturn(getMainCoachDto(id));

        MainCoachDto coachDto = coachService.getByUserId(id);

        Assertions.assertEquals(id, coachDto.getId());
    }

    @Test
    public void getByIdIncorrectTest() {
        int incorrectId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> coachService.getById(incorrectId));
    }

    @Test
    public void getByUserIdIncorrectTest() {
        int incorrectId = 1;

        Mockito.when(coachDao.getByUserId(incorrectId)).thenThrow(NoResultException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> coachService.getById(incorrectId));
    }

    @Test
    public void getAllTest() {
        Mockito.when(coachDao.getAll()).thenAnswer((Answer<List<Coach>>) invocationOnMock -> {
            List<Coach> list = new ArrayList<>();
            list.add(getCoach(1));
            return list;
        });

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenAnswer((Answer<MainCoachDto>) invocationOnMock -> {
                    Coach coach = invocationOnMock.getArgument(0);
                    return getMainCoachDto(coach.getId());
                });

        List<MainCoachDto> mainCoachDtos = coachService.getAll();

        Assertions.assertTrue(mainCoachDtos.size() > 0);
    }

    @Test
    public void getByIdAndLocationIdTest() {
        int coachId = 1;
        int locationId = 1;

        Mockito.when(coachDao.getByIdAndLocationId(coachId, locationId))
                .thenAnswer((Answer<Coach>) invocationOnMock -> getCoach(invocationOnMock.getArgument(0)));

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenAnswer((Answer<MainCoachDto>) invocationOnMock -> {
                    Coach coach = invocationOnMock.getArgument(0);
                    return getMainCoachDto(coach.getId());
                });

        MainCoachDto mainCoachDto = coachService.getByIdAndLocationId(coachId, locationId);

        Assertions.assertEquals(coachId, mainCoachDto.getId());
    }

    @Test
    public void getByIdAndLocationTestIncorrectTest() {
        int incorrectCoachId = 1;
        int incorrectLocationId = 1;
        Mockito.when(coachDao.getByIdAndLocationId(incorrectCoachId, incorrectLocationId))
                .thenThrow(NoResultException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> coachService.getByIdAndLocationId(incorrectCoachId, incorrectLocationId));
    }

    @Test
    public void getAllByLocationIdTest() {
        int locationId = 1;
        Mockito.when(coachDao.getAllByLocationId(locationId)).thenAnswer(new Answer<List<Coach>>() {
            @Override
            public List<Coach> answer(InvocationOnMock invocationOnMock) throws Throwable {
                List<Coach> list = new ArrayList<>();
                list.add(getCoach(1));
                return list;
            }
        });

        Mockito.when(mapper.map(Mockito.any(Coach.class), Mockito.eq(MainCoachDto.class)))
                .thenAnswer((Answer<MainCoachDto>) invocationOnMock -> {
                    Coach coach = invocationOnMock.getArgument(0);
                    return getMainCoachDto(coach.getId());
                });

        List<MainCoachDto> list = coachService.getAllByLocationId(locationId);

        Assertions.assertTrue(list.size() > 0);
    }

    @Test
    public void getSuitableCoachTest() {
        Mockito.when(matcher.getSuitableCoach(Lists.newArrayList(getCoaches()), getCoachCriteria())).thenReturn(Lists.newArrayList(getCoaches()));
        List<CriteriaCoachDto> coaches = matcher.getSuitableCoach(Lists.newArrayList(getCoaches()), getCoachCriteria());
        Assertions.assertTrue(coaches.size() != 0);
    }

    private Coach getCoach(int id) {
        SportType sportType = SportType.builder()
                .id(1)
                .name("Running")
                .build();
        SportType[] sportTypes = {sportType};

        Link link = Link.builder()
                .id(1)
                .url("http://test")
                .type(LinkType.YOUTUBE)
                .build();
        Link[] links = {link};

        return Coach.builder()
                .id(id)
                .rating(1)
                .workWithChildren(false)
                .sportTypes(new HashSet<>(Arrays.asList(sportTypes)))
                .links(new HashSet<>(Arrays.asList(links)))
                .build();
    }

    private CoachDto getCoachDto() {
        SportTypeDto sportType = SportTypeDto.builder()
                .id(1)
                .name("Running")
                .build();
        SportTypeDto[] sportTypes = {sportType};

        LinkDto linkDto = LinkDto.builder()
                .url("http://test")
                .type("youtube")
                .build();
        LinkDto[] links = {linkDto};

        return CoachDto.builder()
                .rating(1)
                .userId(1)
                .workWithChildren(false)
                .sportTypes(new HashSet<>(Arrays.asList(sportTypes)))
                .links(new HashSet<>(Arrays.asList(links)))
                .build();
    }

    private MainCoachDto getMainCoachDto(int id) {
        SportTypeDto sportType = SportTypeDto.builder()
                .id(1)
                .name("Running")
                .build();
        SportTypeDto[] sportTypes = {sportType};

        MainLinkDto linkDto = MainLinkDto.builder()
                .id(1)
                .url("http://test")
                .type("youtube")
                .build();
        MainLinkDto[] links = {linkDto};

        return MainCoachDto.builder()
                .id(id)
                .rating(1)
                .userId(1)
                .workWithChildren(false)
                .sportTypes(new HashSet<>(Arrays.asList(sportTypes)))
                .links(new HashSet<>(Arrays.asList(links)))
                .build();
    }


    private Location getLocation(int locationId) {
        return Location.builder()
                .id(locationId)
                .locationType(LocationType.builder()
                        .id(3)
                        .name("Gym")
                        .build())
                .address(Address.builder()
                        .id(3)
                        .latitude(14.51)
                        .longitude(45.6)
                        .build())
                .name("First-step")
                .build();
    }

    private MainUserDto getMainUser(int id) {
        AddressDto userAddress = AddressDto.builder()
                .latitude(49.843813)
                .longitude(24.035848)
                .build();

        Set<RoleDto> roles = new HashSet<>();
        roles.add(RoleDto.builder().id(2).name("COACH").build());
        return MainUserDto.builder()
                .id(id)
                .roles(roles)
                .gender("female")
                .address(userAddress)
                .build();
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


    private CoachCriteriaDto getCoachCriteria() {
        return CoachCriteriaDto.builder()
                .rating(5.00)
                .sportType("Football")
                .gender("female")
                .workWithChildren(false)
                .locationName("YogaMaster")
                .userId(1)
                .build();
    }


}
