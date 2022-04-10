package com.epam.spsa.service;

import com.epam.spsa.controller.builder.UserInfo;
import com.epam.spsa.dao.EventDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.event.EventDto;
import com.epam.spsa.dto.event.MainEventDto;
import com.epam.spsa.dto.user.MainUserDto;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.model.Event;
import com.epam.spsa.model.User;
import com.epam.spsa.service.impl.EventServiceImpl;
import com.epam.spsa.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

import static org.mockito.ArgumentMatchers.anyInt;

@ExtendWith(SpringExtension.class)
public class EventServiceTest {

    @InjectMocks
    private EventServiceImpl eventService;

    @Mock
    private EventDao eventDao;

    @Mock
    private UserDao userDao;

    @Mock
    private UserServiceImpl userService;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private SlsaClient slsaClient;

    @Mock
    private TaskScheduler taskScheduler;

    @Test
    public void saveEventTest(){
        EventDto eventDto = getEventDto();

        Mockito.when(slsaClient.getById(anyInt())).thenReturn(null);

        Mockito.when(userDao.getById(anyInt())).thenReturn(getUser());

        Mockito.when(modelMapper.map(Mockito.any(EventDto.class),Mockito.eq(Event.class)))
                .thenReturn(getEvent());

        Mockito.when(eventDao.save(Mockito.any(Event.class))).thenReturn(getEvent());

        Mockito.when(modelMapper.map(Mockito.any(Event.class),Mockito.eq(MainEventDto.class)))
                .thenReturn(getMainEventDto());

        MainEventDto mainEventDto = eventService.save(eventDto);

        Assertions.assertEquals(mainEventDto,getMainEventDto());
    }

    @Test
    public void updateEventTest(){
        EventDto eventDto = getEventDto();

        String editedDescription = "Another description";

        Mockito.when(slsaClient.getById(anyInt())).thenReturn(null);

        Mockito.when(eventDao.getById(anyInt())).thenReturn(getEvent());

        Mockito.when(modelMapper.map(Mockito.any(EventDto.class),Mockito.eq(Event.class)))
                .thenReturn(getEvent());

        Mockito.when(userDao.getById(anyInt())).thenReturn(getUser());

        Mockito.when(eventDao.update(Mockito.any(Event.class)))
                .thenReturn(getEvent());

        Mockito.when(modelMapper.map(Mockito.any(Event.class), Mockito.eq(MainEventDto.class)))
                .thenAnswer((Answer<MainEventDto>) invocationOnMock -> {
                    MainEventDto event = getMainEventDto();
                    event.setDescription(editedDescription);
                    return event;
                });

        MainEventDto mainEventDto = eventService.update(eventDto, 1);

        Assertions.assertEquals(mainEventDto.getDescription(), editedDescription);
    }

    @Test
    public void deleteEventTest(){

        Mockito.when(eventDao.getById(anyInt())).thenReturn(getEvent());

        Mockito.when(userDao.getById(anyInt())).thenReturn(getUser());

        Mockito.when(userService.currentUser()).thenReturn(getMainUserDto());

        Mockito.when(userDao.update(Mockito.any(User.class))).thenReturn(getUser());

        Assertions.assertDoesNotThrow(()-> eventService.delete(1));
    }


    @Test
    public void getEventByIdTest(){
        Mockito.when(eventDao.getById(anyInt())).thenReturn(getEvent());

        Mockito.when(modelMapper.map(Mockito.any(Event.class),Mockito.eq(MainEventDto.class)))
                .thenReturn(getMainEventDto());

        MainEventDto mainEventDto = eventService.getById(1);

        Assertions.assertEquals(mainEventDto, getMainEventDto());
    }

    @Test
    public void getEventByUserIdTest(){

        Mockito.when(userDao.getById(anyInt())).thenReturn(getUser());

        Mockito.when(modelMapper.map(Mockito.any(Event.class),Mockito.eq(MainEventDto.class)))
                .thenReturn(getMainEventDto());

        List<MainEventDto> mainEventDtoList = eventService.getByUserId(1);

        Assertions.assertTrue(mainEventDtoList.size()>0);
    }

    @Test
    public void getNotificationsByUserId(){
        Mockito.when(userDao.getById(anyInt())).thenReturn(getUser());

        Assertions.assertDoesNotThrow(() -> eventService.getNotificationByUserId(1));
    }

    @Test
    public void getAllEvents(){
        Mockito.when(eventDao.getAll()).thenReturn(Collections.singletonList(getEvent()));

        Mockito.when(modelMapper.map(Mockito.any(Event.class),Mockito.eq(MainEventDto.class)))
                .thenReturn(getMainEventDto());

        List<MainEventDto> mainEventDtoList = eventService.getAll();

        Assertions.assertTrue(mainEventDtoList.size()>0);
    }


    private EventDto getEventDto(){
        return EventDto.builder()
                .time("10:10:10")
                .location_id(1)
                .date("2020-10-10")
                .description("sport event")
                .owner_id(1)
                .userIdList(new HashSet<>(Collections.singletonList(1)))
                .build();
    }

    private MainEventDto getMainEventDto(){
        return MainEventDto.builder()
                .id(1)
                .time("10:10:10")
                .location_id(1)
                .date("2020-10-10")
                .description("sport event")
                .owner_id(1)
                .userIdList(Collections.singletonList(1))
                .build();
    }

    private Event getEvent(){
        return Event.builder()
                .id(1)
                .time(Time.valueOf("10:10:10"))
                .location_id(1)
                .date(LocalDate.parse("2020-10-10"))
                .description("sport event")
                .owner(getUser())
                .attendee(Collections.singletonList(getUser()))
                .build();
    }

    public User getUser() {
        return User.builder()
                .id(1)
                .email(UserInfo.email)
                .name(UserInfo.name)
                .phoneNumber(UserInfo.phoneNumber)
                .ownEvents(Collections.singletonList(Event.builder().id(2).build()))
                .events(new HashSet<>())
                .build();
    }

    public MainUserDto getMainUserDto() {
        return MainUserDto.builder()
                .id(1)
                .email(UserInfo.email)
                .name(UserInfo.name)
                .phoneNumber(UserInfo.phoneNumber)
                .build();
    }


}
