package com.epam.slsa.service.impl;

import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dao.LocationScheduleDao;
import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.dto.locationSchedule.MainLocationScheduleDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.Day;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.LocationSchedule;
import com.epam.slsa.service.LocationScheduleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class LocationScheduleServiceImpl implements LocationScheduleService {

    private final LocationScheduleDao locationScheduleDao;

    private final LocationDao locationDao;

    private final ModelMapper mapper;

    @Value("${location.exception.notfound}")
    private String locationNotFoundMessage;

    @Value("${locationSchedule.exception.notfound}")
    private String locationScheduleNotFoundMessage;

    @Override
    public Set<MainLocationScheduleDto> save(Set<LocationScheduleDto> locationScheduleDto, int locationId) {
        log.info("Getting Set of LocationScheduleDto. locationId: {}", locationId);
        log.debug("Set<LocationScheduleDto>.size: {}", locationScheduleDto.size());

        Location location = getLocationById(locationId);

        locationScheduleDto = filterExisting(locationScheduleDto, locationId);

        List<LocationSchedule> locationSchedules = locationScheduleDto.stream()
                .map(nls -> {
                    LocationSchedule locationSchedule = mapper.map(nls, LocationSchedule.class);
                    locationSchedule.setLocation(location);
                    return locationSchedule;
                }).collect(Collectors.toList());
        locationSchedules.forEach(locationScheduleDao::save);

        log.info("After saving: Set<LocationScheduleDto>.size: {}", locationScheduleDto.size());
        return locationSchedules.stream()
                .map(ls -> mapper.map(ls, MainLocationScheduleDto.class))
                .collect(Collectors.toSet());
    }

    @Override
    public Set<LocationScheduleDto> update(Set<LocationScheduleDto> editedLocationScheduleDto, int locationId) {
        log.info("Getting List of LocationScheduleDto. locationId: {}", locationId);
        log.debug("List<LocationScheduleDto>.size: {}", editedLocationScheduleDto.size());
        List<LocationSchedule> oldLocationSchedule;
        oldLocationSchedule = locationScheduleDao.getAllByLocationId(locationId);
        updateLocationScheduleFromDto(editedLocationScheduleDto, oldLocationSchedule);
        oldLocationSchedule.forEach(locationScheduleDao::save);

        log.info("After updating: List<LocationScheduleDto>.size: {}", editedLocationScheduleDto.size());
        return editedLocationScheduleDto;
    }

    public void delete(int scheduleId) {
        LocationSchedule locationSchedule = locationScheduleDao.getById(scheduleId);
        if (locationSchedule == null) {
            throw new EntityNotFoundException(locationScheduleNotFoundMessage + scheduleId);
        }
        locationScheduleDao.delete(locationSchedule);
    }

    @Override
    public Set<MainLocationScheduleDto> getAllByLocationId(int id) {
        log.info("Getting Set of LocationScheduleDto by LocationId: {}", id);
        getLocationById(id);
        return locationScheduleDao
                .getAllByLocationId(id)
                .stream()
                .map(ls -> mapper.map(ls, MainLocationScheduleDto.class))
                .collect(Collectors.toSet());
    }

    private void updateLocationScheduleFromDto(Set<LocationScheduleDto> editedLocationSchedule,
                                               List<LocationSchedule> locationSchedules) {
        log.debug("Updating Set of LocationSchedule from Set of LocationScheduleDto");
        for (LocationScheduleDto els : editedLocationSchedule) {
            for (LocationSchedule ls : locationSchedules) {
                if (ls.getDay().equals(Day.fromName(els.getDay()))) {
                    mapper.map(els, ls);
                    break;
                }
            }
        }
    }

    private Set<LocationScheduleDto> filterExisting(Set<LocationScheduleDto> locationScheduleDto, int locationId) {
        List<LocationSchedule> allByLocationId = locationScheduleDao.getAllByLocationId(locationId);
        return locationScheduleDto.stream().filter(
                lsd -> {
                    LocationSchedule mappedLsd = mapper.map(lsd, LocationSchedule.class);
                    return !allByLocationId.contains(mappedLsd);
                }
        ).collect(Collectors.toSet());
    }

    private Location getLocationById(int locationId) {
        Location location = locationDao.getById(locationId);
        if (location == null) {
            throw new EntityNotFoundException(locationNotFoundMessage + locationId);
        }
        return location;
    }

}
