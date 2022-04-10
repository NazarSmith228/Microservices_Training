package com.epam.slsa.service;

import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.location.MainLocationDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LocationService {

    MainLocationDto save(LocationDto newLocationDto);

    LocationDto update(LocationDto locationDto, int locationId);

    void delete(int id);

    MainLocationDto getById(int id) throws Exception;

    List<MainLocationDto> getByName(String name);

    List<MainLocationDto> getByAdminId(int adminId);

    MainLocationDto getByPhoneNumber(String phoneNumber);

    List<MainLocationDto> getAll();

    MainLocationDto saveImage(MultipartFile multipartFile, int id);

    MainLocationDto saveImages(String[] url, int id);

    void deleteImage(int locationId, int imageId);

    MainLocationDto updateImage(MultipartFile multipartFile, int locationId, int imageId);

    MainLocationDto updateImage(String url, int locationId, int imageId);

    void setAdmin(int locationId, int adminId);

}
