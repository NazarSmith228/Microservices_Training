package com.epam.spsa.service.impl;

import com.epam.spsa.dao.FormDao;
import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dto.form.FormDto;
import com.epam.spsa.dto.form.MainFormDto;
import com.epam.spsa.dto.pagination.PaginationDto;
import com.epam.spsa.error.exception.AccessDeniedException;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.error.exception.EntityRoleException;
import com.epam.spsa.feign.MailClient;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.address.DetailedAddressDto;
import com.epam.spsa.feign.dto.coach.CoachDto;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import com.epam.spsa.model.Form;
import com.epam.spsa.model.Role;
import com.epam.spsa.model.User;
import com.epam.spsa.security.oauth2.user.UserPrincipal;
import com.epam.spsa.service.FormService;
import com.epam.spsa.service.UserService;
import com.epam.spsa.utils.PaginationUtils;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class FormServiceImpl implements FormService {

    private final FormDao formDao;
    private final UserDao userDao;

    private final UserService userService;

    private final MailClient mailClient;
    private final SlsaClient slsaClient;

    private final ModelMapper mapper;

    @Value("${form.exception.notFound}")
    private String formNotFoundMessage;

    @Value("${location.exception.notFound}")
    private String locationNotFoundByAddressMessage;

    @Value("${user.auth.notFound}")
    private String authUserNotFoundMessage;

    @Value("${user.delete.otherForm}")
    private String userDeleteOtherFormMessage;

    @Value("${form.role.notSupported}")
    private String formRoleNotSupportedMessage;

    @Value("${form.exception.alreadyExists}")
    private String formAlreadyExistsMessage;

    @Override
    public int save(FormDto newForm) {
        Form form = mapper.map(newForm, Form.class);

        User user;
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            user = ((UserPrincipal) principal).getUser();
        } else {
            throw new EntityNotFoundException(authUserNotFoundMessage);
        }

        if (!form.getRole().getName().equals("COACH")
                && !form.getRole().getName().equals("VENUE_ADMIN")) {
            throw new EntityRoleException(formRoleNotSupportedMessage);
        }

        List<Form> forms = formDao.getByUserId(user.getId());
        if (forms.stream().anyMatch(f -> f.getRole().getName().equals(form.getRole().getName()))) {
            throw new EntityAlreadyExistsException(formAlreadyExistsMessage);
        }

        DetailedAddressDto byCoordinates;
        try {
            byCoordinates = slsaClient.getByCoordinates(form.getLatitude(), form.getLongitude());
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(locationNotFoundByAddressMessage);
        }
        MainLocationDto locationDto = slsaClient.getById(byCoordinates.getLocationId());
        form.setUser(user);
        form.setLocationId(locationDto.getId());
        formDao.save(form);
        return form.getId();
    }

    @Override
    public MainFormDto getById(int id) {
        return mapper.map(getFormById(id), MainFormDto.class);
    }

    @Override
    public PaginationDto<MainFormDto> getAll(int pageNumber, int pageSize) {
        List<Form> forms = formDao.getAll();
        PaginationDto<Form> paginate = PaginationUtils.paginate(forms, pageNumber, pageSize);

        List<MainFormDto> resultList = paginate.getEntities().stream()
                .map(a -> mapper.map(a, MainFormDto.class))
                .collect(Collectors.toList());

        return PaginationDto.<MainFormDto>builder()
                .entities(resultList)
                .quantity(paginate.getQuantity())
                .entitiesLeft(paginate.getEntitiesLeft())
                .build();
    }

    @Override
    public void approve(int id) {
        Form form = getFormById(id);
        User user = form.getUser();

        if (!user.getRoles().contains(form.getRole())) {
            user.getRoles().add(form.getRole());
            userDao.update(user);

            mailClient.sendEmail(
                    "Request approved",
                    user.getEmail(),
                    "Your request for " + form.getRole().getName().toLowerCase() + " privilege has been approved");
        }

        handleForm(user, form);
    }

    @Override
    public void delete(int id) {
        Form formById = getFormById(id);

        if (formById.getUser().getId() != userService.currentUser().getId()) {
            throw new AccessDeniedException(userDeleteOtherFormMessage);
        }

        formDao.delete(formById);
    }

    @Transactional
    public void handleForm(User user, Form form) {
        Role superAdminRole = Role.builder().id(4).name("VENUE_ADMIN").build();
        Role coachRole = Role.builder().id(2).name("COACH").build();
        if (form.getRole().equals(superAdminRole)) {
            log.info("Add admin to location with id: {}", form.getLocationId());
            slsaClient.setAdmin(form.getLocationId(), user.getId());
            mailClient.sendEmail("New location", user.getEmail(), "You have been assigned to the new location as a admin");
        } else if (form.getRole().equals(coachRole)) {
            log.info("Add coach to location with id: {}", form.getLocationId());
            try {
                MainCoachDto coachDto = slsaClient.getByEmail(user.getEmail());
                slsaClient.setNewLocationByCoachId(coachDto.getId(), form.getLocationId());
            } catch (FeignException.NotFound ex) {
                CoachDto coachDto = new CoachDto();
                coachDto.setRating(0);
                coachDto.setUserId(user.getId());
                coachDto.setLinks(new HashSet<>());
                slsaClient.saveCoachInLocation(coachDto, form.getLocationId());
            }
            mailClient.sendEmail("New location", user.getEmail(), "You have been assigned to the new location as a coach");
        }
        formDao.delete(form);
    }

    private Form getFormById(int id) {
        Form form = formDao.getById(id);
        if (form == null) {
            log.info(formNotFoundMessage + id);
            throw new EntityNotFoundException(formNotFoundMessage + id);
        }
        return form;
    }

}
