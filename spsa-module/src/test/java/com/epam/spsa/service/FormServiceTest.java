package com.epam.spsa.service;

import com.epam.spsa.dao.FormDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityRoleException;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.feign.dto.address.DetailedAddressDto;
import com.epam.spsa.feign.dto.coach.CoachDto;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import com.epam.spsa.model.Form;
import com.epam.spsa.model.Role;
import com.epam.spsa.model.User;
import com.epam.spsa.security.JwtAuthenticationToken;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.impl.FormServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
public class FormServiceTest {

    @InjectMocks
    private FormServiceImpl formService;

    @Mock
    private FormDao formDao;

    @Mock
    private UserDao userDao;

    @Mock
    private MailClient mailClient;

    @Mock
    private SlsaClient slsaClient;

    @Mock
    private ModelMapper mapper;

    @BeforeEach
    public void classSetUp() {
        SecurityContextHolder
                .getContext()
                .setAuthentication(getAuthentication());
    }

    @Test
    public void successfulSaveTest() {
        int id = 1;

        Form form = getForm();
        DetailedAddressDto detailedAddressDto = getDetailedAddressDto();
        MainLocationDto mainLocationDto = getMainLocationDto();

        when(mapper.map(eq(getFormDto()), eq(Form.class))).thenReturn(form);

        when(slsaClient.getByCoordinates(form.getLatitude(), form.getLongitude()))
                .thenReturn(detailedAddressDto);

        when(slsaClient.getById(detailedAddressDto.getLocationId()))
                .thenReturn(mainLocationDto);

        when(formDao.save(any(Form.class))).thenAnswer(invocation -> {
            Form argument = invocation.getArgument(0);
            argument.setId(id);
            return argument;
        });

        int result = formService.save(getFormDto());
        assertEquals(id, result);
        assertEquals(form.getLocationId(), mainLocationDto.getId());
    }

    @Test
    public void locationNotFoundSaveTest() {
        Form form = getForm();

        when(mapper.map(eq(getFormDto()), eq(Form.class))).thenReturn(getForm());
        when(slsaClient.getByCoordinates(form.getLatitude(), form.getLongitude()))
                .thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> formService.save(getFormDto()));
    }

    @Test
    public void roleAlreadyExistsSaveTest() {
        Form form = getForm();

        when(mapper.map(eq(getFormDto()), eq(Form.class))).thenReturn(form);

        when(formDao.getByUserId(anyInt())).thenReturn(Collections.singletonList(form));

        Assertions.assertThrows(EntityAlreadyExistsException.class,
                () -> formService.save(getFormDto()));
    }

    @Test
    public void unsupportedRoleSaveTest() {
        Form form = getForm();
        form.setRole(Role.builder().id(1).name("USER").build());

        when(mapper.map(eq(getFormDto()), eq(Form.class))).thenReturn(form);

        Assertions.assertThrows(EntityRoleException.class,
                () -> formService.save(getFormDto()));
    }

    @Test
    public void formNotFoundGetByIdTest() {
        when(formDao.getById(anyInt())).thenReturn(null);
        assertThrows(EntityNotFoundException.class, () -> formService.getById(1));
    }

    @Test
    public void successfulGetByIdTest() {
        MainFormDto mainFormDto = getMainFormDto();

        when(mapper.map(any(), eq(MainFormDto.class))).thenReturn(mainFormDto);
        when(formDao.getById(anyInt())).thenReturn(getForm());

        MainFormDto byId = formService.getById(1);
        assertEquals(mainFormDto.getLocationName(), byId.getLocationName());
    }

    @Test
    public void approveExistingCoachForm() {
        Form form = getForm();
        MainCoachDto coachDto = getMainCoachDto();

        when(formDao.getById(anyInt())).thenReturn(form);
        when(userDao.update(any())).thenReturn(form.getUser());

        doNothing().when(mailClient).sendEmail(anyString(), anyString(), anyString());
        when(slsaClient.getByEmail(anyString())).thenReturn(coachDto);
        doNothing().when(slsaClient).setNewLocationByCoachId(eq(coachDto.getId()), eq(form.getLocationId()));
        doNothing().when(formDao).delete(any());

        formService.approve(1);

        verify(userDao).update(any());
        verify(mailClient, times(2)).sendEmail(anyString(), anyString(), anyString());
        verify(slsaClient).getByEmail(anyString());
        verify(slsaClient).setNewLocationByCoachId(eq(coachDto.getId()), eq(form.getLocationId()));
    }

    @Test
    public void approveNonExistingCoachForm() {
        Form form = getForm();

        when(formDao.getById(anyInt())).thenReturn(form);
        when(userDao.update(any())).thenReturn(form.getUser());

        doNothing().when(mailClient).sendEmail(anyString(), anyString(), anyString());
        when(slsaClient.getByEmail(anyString())).thenThrow(FeignException.NotFound.class);
        doReturn(null).when(slsaClient).saveCoachInLocation(any(CoachDto.class), eq(form.getLocationId()));
        doNothing().when(formDao).delete(any());
        when(mapper.map(eq(form.getUser()), eq(CoachDto.class))).thenReturn(CoachDto.builder().build());

        formService.approve(1);

        verify(userDao).update(any());
        verify(mailClient, times(2)).sendEmail(anyString(), anyString(), anyString());
        verify(slsaClient).getByEmail(anyString());
        verify(slsaClient).saveCoachInLocation(any(CoachDto.class), eq(form.getLocationId()));
    }

    @Test
    public void approveNonExistingAdminForm() {
        Form form = getForm(Role.builder().id(4).name("VENUE_ADMIN").build());

        when(formDao.getById(anyInt())).thenReturn(form);
        when(userDao.update(any())).thenReturn(form.getUser());

        doNothing().when(mailClient).sendEmail(anyString(), anyString(), anyString());
        doNothing().when(slsaClient).setAdmin(eq(form.getLocationId()), eq(form.getUser().getId()));

        formService.approve(1);

        verify(userDao).update(any());
        verify(mailClient, times(2)).sendEmail(anyString(), anyString(), anyString());
        verify(slsaClient).setAdmin(eq(form.getLocationId()), eq(form.getUser().getId()));
    }

    private static MainCoachDto getMainCoachDto() {
        return MainCoachDto.builder()
                .id(1)
                .build();
    }

    private static DetailedAddressDto getDetailedAddressDto() {
        return DetailedAddressDto.builder()
                .latitude(10.0)
                .longitude(20.0)
                .locationId(1)
                .build();
    }

    private static MainLocationDto getMainLocationDto() {
        return MainLocationDto.builder()
                .id(1)
                .name("Test Location")
                .build();
    }

    private static MainFormDto getMainFormDto() {
        return MainFormDto.builder()
                .id(1)
                .locationName("Test location")
                .build();
    }

    private static FormDto getFormDto() {
        return FormDto.builder()
                .locationName("Test")
                .build();
    }

    private static Form getForm(Role role) {
        Form form = getForm();
        form.setRole(role);
        return form;
    }

    private static Form getForm() {
        return Form.builder()
                .latitude(10.0)
                .longitude(20.0)
                .locationId(1)
                .user(getUser())
                .locationName("Test")
                .role(getCoachRole())
                .build();
    }

    private static Role getCoachRole() {
        return Role.builder()
                .id(2)
                .name("COACH")
                .build();
    }

    private static User getUser() {
        Set<Role> roles = new HashSet<>();
        Role user = Role.builder()
                .id(1)
                .name("USER")
                .build();
        roles.add(user);
        return User.builder()
                .name("Test")
                .surname("Test")
                .email("Test")
                .roles(roles)
                .build();
    }

    private static Authentication getAuthentication() {
        JwtAuthenticationToken token = new JwtAuthenticationToken();
        token.setUserPrincipal(UserPrincipal.builder()
                .user(getUser())
                .build());
        return token;
    }

}
