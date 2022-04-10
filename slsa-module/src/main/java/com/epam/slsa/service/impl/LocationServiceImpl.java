package com.epam.slsa.service.impl;

import com.epam.slsa.dao.ImageDao;
import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.error.exception.EntityAlreadyExistsException;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.error.exception.EntityRoleException;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.model.Image;
import com.epam.slsa.model.Location;
import com.epam.slsa.s3api.S3Manager;
import com.epam.slsa.service.LocationService;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@PropertySource(value = "classpath:/messages.properties")
@Slf4j
public class LocationServiceImpl implements LocationService {

    private final LocationDao locationDao;

    private final ImageDao imageDao;

    private final ModelMapper mapper;

    private final S3Manager s3Manager;

    private final PartnerClient partnerClient;

    @Value("${location.exception.notfound}")
    private String locationIdNotFound;

    @Value("${location.exception.phoneNumber.notfound}")
    private String locationPhoneNotFound;

    @Value("${location.exception.phoneNumber.exists}")
    private String locationPhoneAlreadyExists;

    @Value("${location.exception.notfoundimage}")
    private String imageIdNotFound;

    @Value("${user.exception.notfound}")
    private String userNotFoundMessage;

    @Value("${user.exception.roleAdmin}")
    private String userAdminRoleMessage;

    @Override
    public MainLocationDto save(LocationDto newLocationDto) {
        log.info("Saving LocationDto: {}", newLocationDto);
        assertExists(() -> getByPhoneNumber(newLocationDto.getPhoneNumber()), locationPhoneAlreadyExists);

        Location location = mapper.map(newLocationDto, Location.class);
        location.getAddress().setLocation(null);
        return mapper.map(locationDao.save(location), MainLocationDto.class);
    }

    @Override
    public LocationDto update(LocationDto locationDto, int locationId) {
        log.info("Updating LocationDto: {} by locationId: {}", locationDto, locationId);
        Location oldLocation = getLocationById(locationId);

        if ((oldLocation.getPhoneNumber() != null
                && locationDto.getPhoneNumber() != null)
                && !locationDto.getPhoneNumber().equals(oldLocation.getPhoneNumber())) {
            assertExists(() -> getByPhoneNumber(locationDto.getPhoneNumber()), locationPhoneAlreadyExists);
        }

        mapper.map(locationDto, oldLocation);
        return mapper.map(locationDao.update(oldLocation), LocationDto.class);
    }

    @Override
    public void delete(int id) {
        log.info("Deleting Location by id: {}", id);
        Location deletedLocation = getLocationById(id);
        locationDao.delete(deletedLocation);
    }

    @Override
    public MainLocationDto getById(int id) {
        log.info("Getting MainLocationDto by id: {}", id);
        Location location = getLocationById(id);
        return mapper.map(location, MainLocationDto.class);
    }

    @Override
    public List<MainLocationDto> getByName(String name) {
        log.info("Getting List of MainLocationDto by name: {}", name);
        return locationDao.getByName(name)
                .stream()
                .map(l -> mapper.map(l, MainLocationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<MainLocationDto> getByAdminId(int adminId) {
        try {
            MainUserDto user = partnerClient.getUserById(adminId);
            boolean notVenueAdmin = user.getRoles()
                    .stream()
                    .noneMatch(roleDto -> roleDto.getName().equalsIgnoreCase("venue_admin"));
            if (notVenueAdmin) {
                throw new EntityRoleException(userAdminRoleMessage);
            }
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(userNotFoundMessage + adminId);
        }

        return locationDao.getByAdminId(adminId)
                .stream()
                .map(l -> mapper.map(l, MainLocationDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public MainLocationDto getByPhoneNumber(String phoneNumber) {
        try {
            return mapper.map(locationDao.getByPhoneNumber(phoneNumber), MainLocationDto.class);
        } catch (NoResultException | EmptyResultDataAccessException ex) {
            throw new EntityNotFoundException(locationPhoneNotFound + phoneNumber);
        }
    }

    @Override
    public List<MainLocationDto> getAll() {
        log.info("Getting List of MainLocationDto");
        return locationDao.getAll()
                .stream()
                .map(l -> mapper.map(l, MainLocationDto.class))
                .collect(Collectors.toList());
    }

    private Location getLocationById(int id) {
        log.info("Getting Location by id: {}", id);
        Location location = locationDao.getById(id);
        if (location == null) {
            log.error("Location wasn't found. id: {}", id);
            throw new EntityNotFoundException(locationIdNotFound + id);
        }
        log.debug("Result Location: {}", location);
        return location;
    }


    @Override
    public MainLocationDto saveImage(MultipartFile multipartFile, int id) {
        log.info("Saving new image to location by id: {}", id);
        Location location = getLocationById(id);
        Image image = Image.builder().location(location).build();

        imageDao.save(image);

        String url = s3Manager.saveImageById(multipartFile, id,
                "locations/location_" + id, image.getId() + "_photo");
        log.info("Url image: {}", url);

        image.setUrl(url);
        imageDao.update(image);

        log.info("Result Location: {}", location);
        return mapper.map(location, MainLocationDto.class);
    }

    @Override
    public MainLocationDto saveImages(String[] url, int id) {
        log.info("Saving new image-urls to location by id: {}", id);
        Location location = getLocationById(id);

        for (String s : url) {
            log.info("Url image: {}", s);
            Image image = Image.builder()
                    .url(s)
                    .location(location)
                    .build();
            imageDao.save(image);
        }

        log.info("Result Location: {}", location);
        return mapper.map(location, MainLocationDto.class);
    }

    @Override
    public void deleteImage(int locationId, int imageId) {
        log.info("Deleting image with id: {}", imageId);
        Image image = imageDao.getById(imageId);

        if (image == null) {
            log.error("Image wasn't found. id: {}", imageId);
            throw new EntityNotFoundException(imageIdNotFound + imageId);
        }

        imageDao.delete(image);
        s3Manager.deleteImage(locationId, "locations/location_" + locationId,
                imageId + "_photo");
    }

    @Override
    public MainLocationDto updateImage(MultipartFile multipartFile, int locationId, int imageId) {
        log.info("Updating image with id: {}", imageId);
        Image image = imageDao.getById(imageId);

        if (image == null) {
            log.error("Image wasn't found. id: {}", imageId);
            throw new EntityNotFoundException(imageIdNotFound + imageId);
        }

        String url = s3Manager.saveImageById(multipartFile, locationId,
                "locations/location_" + locationId, image.getId() + "_photo");
        image.setUrl(url);
        imageDao.update(image);

        log.info("Result photo: {}", image);
        return mapper.map(getLocationById(locationId), MainLocationDto.class);
    }

    @Override
    public MainLocationDto updateImage(String url, int locationId, int imageId) {
        log.info("Updating image with id: {}", imageId);
        Image image = imageDao.getById(imageId);

        if (image == null) {
            log.error("Image wasn't found. id: {}", imageId);
            throw new EntityNotFoundException(imageIdNotFound + imageId);
        }

        s3Manager.deleteImage(locationId, "locations/location_" + locationId,
                imageId + "_photo");
        image.setUrl(url);
        imageDao.update(image);

        log.info("Result image: {}", image);
        return mapper.map(getLocationById(locationId), MainLocationDto.class);
    }

    @Override
    public void setAdmin(int locationId, int adminId) {
        Location location = getLocationById(locationId);

        try {
            MainUserDto user = partnerClient.getUserById(adminId);
            if (user.getRoles().stream()
                    .noneMatch(rd -> rd.getName().equalsIgnoreCase("VENUE_ADMIN"))) {
                throw new EntityRoleException(userAdminRoleMessage);
            }
        } catch (FeignException.NotFound ex) {
            throw new EntityNotFoundException(userNotFoundMessage + adminId);
        }

        location.setAdminId(adminId);
        locationDao.update(location);
    }

    private void assertExists(Supplier<MainLocationDto> supplier, String message) {
        try {
            MainLocationDto user = supplier.get();
            if (user != null) {
                throw new EntityAlreadyExistsException(message);
            }
        } catch (EntityNotFoundException ignored) {
        }
    }

}
