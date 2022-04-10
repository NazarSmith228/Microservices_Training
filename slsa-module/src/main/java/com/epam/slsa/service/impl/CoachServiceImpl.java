package com.epam.slsa.service.impl;

import com.epam.slsa.dao.CoachDao;
import com.epam.slsa.dao.LinkDao;
import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dto.coach.CoachDto;
import com.epam.slsa.dto.coach.CriteriaCoachDto;
import com.epam.slsa.dto.coach.MainCoachDto;
import com.epam.slsa.dto.coachCriteria.CoachCriteriaDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.error.exception.EntityAlreadyExistsException;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.error.exception.EntityRoleException;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.match.CoachMatcher;
import com.epam.slsa.model.Coach;
import com.epam.slsa.model.Link;
import com.epam.slsa.model.Location;
import com.epam.slsa.service.CoachService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@PropertySource(value = "classpath:/messages.properties")
@Slf4j
public class CoachServiceImpl implements CoachService {

    private final CoachDao coachDao;

    private final LocationDao locationDao;

    private final ModelMapper mapper;

    private final LinkDao linkDao;

    private final PartnerClient partnerClient;

    private final CoachMatcher coachMatcher;

    @Value("${coach.exception.notfound}")
    private String coachNotFoundByIdMessage;

    @Value("${coach.exception.user.notfound}")
    private String coachNotFoundByUserIdMessage;

    @Value("${user.exception.notfound}")
    private String userNotFoundMessage;

    @Value("${coach.link.exist}")
    private String coachLinkAlreadyExists;

    @Value("${coach.link.notfound}")
    private String linkExceptionMessage;

    @Value("${location.exception.notfound}")
    private String locationIdNotFound;

    @Value("${coach.exists}")
    private String userAlreadyCoachMessage;

    @Value("${user.exception.roleCoach}")
    private String userCoachRoleMessage;

    @Override
    public MainCoachDto save(CoachDto coachDto, int locationId) {
        log.info("Saving CoachDto. locationId: {}", locationId);
        log.debug("CoachDto: {}", coachDto);
        Location location = locationDao.getById(locationId);
        if (location == null) {
            throw new EntityNotFoundException(locationIdNotFound + locationId);
        }

        try {
            MainUserDto userDto = partnerClient.getUserById(coachDto.getUserId());
            if (userDto.getRoles().stream()
                    .noneMatch(rd -> rd.getName().equalsIgnoreCase("COACH"))) {
                throw new EntityRoleException(userCoachRoleMessage);
            }
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(userNotFoundMessage + coachDto.getUserId());
        }
        assertNotExists(
                () -> getByUserId(coachDto.getUserId()),
                userAlreadyCoachMessage);

        Coach coach = mapper.map(coachDto, Coach.class);

        coach.setLocation(location);
        log.debug("Created Coach: {}", coachDto);
        coach.getLinks().forEach(l -> l.setCoach(coach));
        return mapper.map(coachDao.save(coach), MainCoachDto.class);
    }

    @Override
    public MainCoachDto update(CoachDto editedCoachDto, int locationId, int coachId) {
        log.info("Updating Coach by coachId: {} and locationId: {}", coachId, locationId);

        Coach oldCoach = getCoachByIdAndLocationId(coachId, locationId);
        Set<Link> oldLinks = oldCoach.getLinks();

        editedCoachDto.getLinks().forEach(l -> checkUrlExisting(l.getUrl(), oldCoach.getId()));

        int userId = oldCoach.getUserId();
        mapper.map(editedCoachDto, oldCoach);
        oldCoach.setUserId(userId);

        setNewUrls(oldLinks, oldCoach.getLinks(), oldCoach);
        return mapper.map(coachDao.update(oldCoach), MainCoachDto.class);
    }

    @Override
    public void delete(int id) {
        log.info("Deleting Coach by id: {}", id);
        Coach deletedCoach = getCoachById(id);
        coachDao.delete(deletedCoach);
    }

    @Override
    public MainCoachDto getById(int id) {
        log.info("Getting MainCoachDto by id: {}", id);
        Coach coach = getCoachById(id);
        return mapper.map(coach, MainCoachDto.class);
    }

    @Override
    public List<MainCoachDto> getAll() {
        log.info("Getting List of MainCoachDto");
        return coachDao
                .getAll()
                .stream()
                .map(c -> mapper.map(c, MainCoachDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MainCoachDto getByIdAndLocationId(int coachId, int locationId) {
        log.info("Getting MainCoachDto by coachId: {} and locationId: {}", coachId, locationId);
        Coach coach = getCoachByIdAndLocationId(coachId, locationId);
        return mapper.map(coach, MainCoachDto.class);
    }

    @Override
    public List<MainCoachDto> getAllByLocationId(int id) {
        log.info("Getting List of MainCoachDto by id: {}", id);
        return coachDao
                .getAllByLocationId(id)
                .stream()
                .map(c -> mapper.map(c, MainCoachDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MainCoachDto getByUserId(int userId) {
        try {
            return mapper.map(coachDao.getByUserId(userId), MainCoachDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(coachNotFoundByUserIdMessage + userId);
        }
    }

    private Coach getCoachById(int id) {
        log.info("Getting Coach by id: {}", id);
        Coach coach = coachDao.getById(id);
        if (coach == null) {
            log.error("Coach wasn't found. id: {}", id);
            throw new EntityNotFoundException(coachNotFoundByIdMessage + id);
        }
        log.debug("Result Coach: {}", coach);
        return coach;
    }

    private Coach getCoachByIdAndLocationId(int coachId, int locationId) {
        log.info("Getting Coach by id: {}, and locationId: {}", coachId, locationId);
        Coach coach;
        try {
            coach = coachDao.getByIdAndLocationId(coachId, locationId);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(coachNotFoundByIdMessage + coachId
                    + " or " + locationIdNotFound + locationId);
        }
        log.debug("Result Coach: {}", coach);
        return coach;
    }

    @Override
    public void setNewLocationByCoachId(int locationId, int coachId) {
        Coach coach = getCoachById(coachId);
        Location location = locationDao.getById(locationId);
        if (location == null) {
            throw new EntityNotFoundException(locationIdNotFound + locationId);
        }
        coach.setLocation(location);
        coachDao.update(coach);
    }

    public void deleteCoachLinkById(int linkId) {
        log.info("Delete link bu id: {}", linkId);
        Link link = linkDao.getById(linkId);
        if (link == null) {
            log.error("Coach wasn't found. id: {}", linkId);
            throw new EntityNotFoundException(linkExceptionMessage + linkId);
        }
        linkDao.delete(link);
    }

    @Override
    public List<CriteriaCoachDto> getSuitableCoach(CoachCriteriaDto criteria) {
        List<CriteriaCoachDto> coaches = coachDao.getAll()
                .stream()
                .map(c -> {
                    CriteriaCoachDto coach = mapper.map(c, CriteriaCoachDto.class);
                    coach.setLocation(mapper.map(c.getLocation(), MainLocationDto.class));
                    return coach;
                })
                .collect(Collectors.toList());

        return coachMatcher.getSuitableCoach(coaches, criteria);

    }

    private void checkUrlExisting(String url, int coachId) {
        try {
            Link link = linkDao.getByUrl(url);
            if (link.getCoach().getId() != coachId) {
                throw new EntityAlreadyExistsException(coachLinkAlreadyExists);
            }
        } catch (NoResultException | EmptyResultDataAccessException ignored) {
        }
    }

    private void setNewUrls(Set<Link> oldLinks, Set<Link> editedLinks, Coach oldCoach) {
        for (Link link : editedLinks) {
            for (Link oldLink : oldLinks) {
                if ((link.getType().getType()).equals(oldLink.getType().getType())) {
                    link.setId(oldLink.getId());
                }
            }
            link.setCoach(oldCoach);
        }
    }

    private void checkUrlExisting(String url) {
        try {
            Link link = linkDao.getByUrl(url);
            throw new EntityAlreadyExistsException(coachLinkAlreadyExists);
        } catch (NoResultException | EmptyResultDataAccessException ignored) {
        }
    }

    private <T> void assertNotExists(Supplier<T> supplier, String message) {
        try {
            T t = supplier.get();
            if (t != null) {
                throw new EntityAlreadyExistsException(message);
            }
        } catch (EntityNotFoundException
                | NoResultException
                | EmptyResultDataAccessException ignore) {
        }
    }

}
