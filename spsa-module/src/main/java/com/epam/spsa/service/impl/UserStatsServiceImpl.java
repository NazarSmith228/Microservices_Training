package com.epam.spsa.service.impl;

import com.epam.spsa.dao.UserDao;
import com.epam.spsa.dao.UserStatsDao;
import com.epam.spsa.dto.sport.SportTypeDto;
import com.epam.spsa.dto.user.CommonUserStatDto;
import com.epam.spsa.dto.user.MainCommonUserStatDto;
import com.epam.spsa.dto.user.MainUserStatsDto;
import com.epam.spsa.dto.user.UserStatsDto;
import com.epam.spsa.error.exception.DurationDateException;
import com.epam.spsa.error.exception.EntityAlreadyExistsException;
import com.epam.spsa.error.exception.EntityNotFoundException;
import com.epam.spsa.feign.SlsaClient;
import com.epam.spsa.feign.dto.coach.MainCoachDto;
import com.epam.spsa.feign.dto.location.MainLocationDto;
import com.epam.spsa.model.SportType;
import com.epam.spsa.model.User;
import com.epam.spsa.model.UserStats;
import com.epam.spsa.service.UserStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@SuppressWarnings("Duplicates")
public class UserStatsServiceImpl implements UserStatsService {

    private final ModelMapper mapper;

    private final UserStatsDao statsDao;

    private final UserDao userDao;

    private final SlsaClient slsaClient;

    @Value("${user.exception.notfound}")
    private String userNotFoundByIdMessage;

    @Value("${user.stat.dateError}")
    private String dateDurationExp;

    @Override
    public MainUserStatsDto saveUserStats(UserStatsDto userStatsDto, int userId) {
        log.info("Saving stats for user with id={}", userId);
        UserStats stats = saveStats(userStatsDto, userId);
        log.debug("Check for correct mapping: {}", stats);
        return mapper.map(statsDao.save(stats), MainUserStatsDto.class);
    }

    @Override
    public List<MainUserStatsDto> getAllUserStats(int userId) {
        log.info("Getting all stats for user id={}", userId);
        List<UserStats> statsList = getAllStats(userId);
        return statsList.stream().map(s -> mapper.map(s, MainUserStatsDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MainUserStatsDto getLastUserStats(int userId) {
        log.info("Getting last stats for user with id={}", userId);
        return mapper.map(getLastStats(userId), MainUserStatsDto.class);
    }

    @Override
    public MainUserStatsDto updateLastUserStats(UserStatsDto statsDto, int userId) {
        log.info("Updating last stats for user with id={}", userId);
        UserStats stats = updateLastStats(userId, statsDto);
        return mapper.map(statsDao.updateStats(stats), MainUserStatsDto.class);
    }

    @Override
    public void deleteUserStats(int userId) {
        log.info("Check if user exists id={}", userId);
        User ifExists = getUserById(userId);
        statsDao.deleteByUserId(userId);
    }

    private boolean isAcceptable(int userId, LocalDate insertionDate) {
        log.info("Check if there is no record with insertionDate={}", insertionDate);
        try {
            LocalDate lastDate = statsDao.getLastStats(userId).getInsertionDate();
            log.debug("insertion date in db: {}, new insertion date: {}", lastDate, insertionDate);
            return lastDate.compareTo(insertionDate) < 0;
        } catch (EmptyResultDataAccessException e) {
            log.debug("There is no stats for user: {}; ", userId);
            return true;
        }
    }

    private UserStats saveStats(UserStatsDto userStatsDto, int userId) {
        log.info("Check if user exists id={}", userId);
        User userExists = getUserById(userId);

        if (userStatsDto.getLocationId() != 0) {
            log.info("Getting location from slsa, id={}", userStatsDto.getLocationId());
            MainLocationDto locationDto = slsaClient.getById(userStatsDto.getLocationId());

            List<MainCoachDto> coachDto = locationDto.getCoaches();
            log.info("Check if coach exists for specific location from slsa, coach.id={}", userStatsDto.getCoachId());
            boolean coachExists = userStatsDto.getCoachId() == 0 ||
                    coachDto.stream().anyMatch(c -> c.getId() == userStatsDto.getCoachId());
            log.info("Check if sport type exists for specific location from slsa, sportType={}", userStatsDto.getSportType());
            boolean sportTypeExists = locationDto.getSportTypes().contains(userStatsDto.getSportType());
            if (!coachExists) {
                throw new EntityNotFoundException(
                        "Coach with id=" + userStatsDto.getCoachId()
                                + " in location with id=" + userStatsDto.getLocationId()
                                + " not found");
            }
            if (!sportTypeExists) {
                throw new EntityNotFoundException(
                        "Sport type with id=" + userStatsDto.getSportType().getId()
                                + " in location with id=" + userStatsDto.getLocationId()
                                + " not found!"
                );
            }
        }

        boolean acceptableDate = isAcceptable(userId, LocalDate.now());
        if (!acceptableDate) {
            throw new EntityAlreadyExistsException("Inserted date already exists in data base;" +
                    " You can insert stats only once per day");
        }
        UserStats stats = mapper.map(userStatsDto, UserStats.class);
        stats.setUser(userExists);
        stats.setInsertionDate(LocalDate.now());

        updateResult(stats);
        return stats;
    }

    private List<UserStats> getAllStats(int userId) {
        log.info("Check if user exists id={}", userId);
        User ifExists = getUserById(userId);
        List<UserStats> statsList = statsDao.getAllByUserId(userId);
        if (statsList.size() == 0) {
            throw new EntityNotFoundException("Statistics for user with id: " + userId + ", not found!");
        }
        return statsList;
    }

    private UserStats getLastStats(int userId) {
        log.info("Check if user exists id={}", userId);
        User ifExists = getUserById(userId);
        try {
            return statsDao.getLastStats(userId);
        } catch (EmptyResultDataAccessException e) {
            throw new EntityNotFoundException("There is no statistics for user with id=" + userId);
        }
    }

    private UserStats updateLastStats(int userId, UserStatsDto editedStatsDto) {
        log.info("Check if user exists id={}", userId);
        User userExists = getUserById(userId);
        if (editedStatsDto.getLocationId() != 0) {
            log.info("Getting location from slsa, id={}", editedStatsDto.getLocationId());
            MainLocationDto locationDto = slsaClient.getById(editedStatsDto.getLocationId());

            List<MainCoachDto> coachDto = locationDto.getCoaches();
            log.info("Check if coach exists for specific location from slsa, coach.id={}", editedStatsDto.getCoachId());
            boolean coachExists = editedStatsDto.getCoachId() == 0 ||
                    coachDto.stream().anyMatch(c -> c.getId() == editedStatsDto.getCoachId());
            log.info("Check if sport type exists for specific location from slsa, sportType={}", editedStatsDto.getSportType());
            boolean sportTypeExists = locationDto.getSportTypes().contains(editedStatsDto.getSportType());
            if (!coachExists) {
                throw new EntityNotFoundException(
                        "Coach with id=" + editedStatsDto.getCoachId()
                                + " in location with id=" + editedStatsDto.getLocationId()
                                + " not found");
            }
            if (!sportTypeExists) {
                throw new EntityNotFoundException(
                        "Sport type with id=" + editedStatsDto.getSportType().getId()
                                + " in location with id=" + editedStatsDto.getLocationId()
                                + " not found!"
                );
            }
        }

        UserStats stats = mapper.map(editedStatsDto, UserStats.class);
        stats.setUser(userExists);
        stats.setInsertionDate(LocalDate.now());

        updateResult(stats);
        return stats;
    }

    private void updateResult(UserStats stats) {
        if (stats.getSportType().getId() == 4) {
            log.debug("Sport is yoga, so result km = 0");
            stats.setResultKm(0);
        }
    }

    private User getUserById(int id) {
        log.info("Getting User by id: {}", id);
        User user = userDao.getById(id);
        if (user == null) {
            log.error("User wasn't found. id: {}", id);
            throw new EntityNotFoundException(userNotFoundByIdMessage + id);
        }
        log.info("Found User: {}", user);
        return user;
    }

    public MainCommonUserStatDto getCommonStat(CommonUserStatDto statDto, int userId) {
        log.info("Getting common statistic for user with id {}", userId);
        LocalDate startDuration = statDto.getStartOfInterval();
        LocalDate endDuration = statDto.getEndOfInterval();

        if (endDuration.isBefore(startDuration)
                || endDuration.isEqual(startDuration)) {
            log.error("Bad date interval");
            throw new DurationDateException(dateDurationExp);
        }
        getUserById(userId);
        List<UserStats> stats = statsDao.getAllByUserId(userId);

        log.info("Finding statistics that fall into the gap");
        stats.removeIf(userStats ->
                userStats.getInsertionDate().isAfter(endDuration) ||
                        userStats.getInsertionDate().isBefore(startDuration)
        );

        if (statDto.getCoaches() != null) {
            if (statDto.getCoaches().size() != 0) {
                log.info("Finding coaches {} ", statDto.getCoaches());
                stats.removeIf(userStats -> !statDto.getCoaches().contains(userStats.getCoachId()));
            }
        }
        if (statDto.getLocations() != null) {
            if (statDto.getLocations().size() != 0) {
                log.info("Finding locations {} ", statDto.getLocations());
                stats.removeIf(userStats -> !statDto.getLocations().contains(userStats.getLocationId()));
            }
        }
        if (statDto.getSportTypes() != null) {
            if (statDto.getSportTypes().size() != 0) {
                log.info("Finding sport types {}", statDto.getSportTypes());
                stats.removeIf(userStats -> !statDto.getSportTypes()
                        .contains(mapper.map(userStats.getSportType(), SportTypeDto.class)));
            }
        }

        log.info("Calculating common stat.");
        double resultKm = stats.stream().mapToDouble(UserStats::getResultKm).sum();
        long resultH = (long) stats.stream().mapToDouble(value -> value.getResultH().getSeconds()).sum();

        Set<SportType> sportTypes = stats.stream().map(UserStats::getSportType).collect(Collectors.toSet());
        Set<Integer> locations = stats.stream().map(UserStats::getLocationId).collect(Collectors.toSet());
        Set<Integer> coaches = stats.stream().map(UserStats::getCoachId).collect(Collectors.toSet());

        return MainCommonUserStatDto.builder()
                .resultH(Duration.ofSeconds(resultH).toString())
                .resultKm(resultKm)
                .coaches(coaches)
                .locations(locations)
                .sportTypes(sportTypes)
                .build();
    }

}
