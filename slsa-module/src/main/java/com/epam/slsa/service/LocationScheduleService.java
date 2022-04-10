package com.epam.slsa.service;

import com.epam.slsa.dto.locationSchedule.LocationScheduleDto;
import com.epam.slsa.dto.locationSchedule.MainLocationScheduleDto;

import java.util.Set;

public interface LocationScheduleService {

    Set<MainLocationScheduleDto> save(Set<LocationScheduleDto> locationScheduleDto, int locationId);

    Set<LocationScheduleDto> update(Set<LocationScheduleDto> editedLocationScheduleDto, int locationId);

    void delete(int scheduleId);

    Set<MainLocationScheduleDto> getAllByLocationId(int id);

}
