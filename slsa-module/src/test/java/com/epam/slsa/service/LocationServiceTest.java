package com.epam.slsa.service;

import com.epam.slsa.builders.photo.FileBuilder;
import com.epam.slsa.builders.photo.ImageBuilder;
import com.epam.slsa.dao.ImageDao;
import com.epam.slsa.dao.LocationDao;
import com.epam.slsa.dto.address.AddressDto;
import com.epam.slsa.dto.image.MainImageDto;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.error.exception.EntityRoleException;
import com.epam.slsa.feign.PartnerClient;
import com.epam.slsa.feign.dto.MainUserDto;
import com.epam.slsa.feign.dto.RoleDto;
import com.epam.slsa.model.Address;
import com.epam.slsa.model.Image;
import com.epam.slsa.model.Location;
import com.epam.slsa.model.LocationType;
import com.epam.slsa.s3api.S3Manager;
import com.epam.slsa.service.impl.LocationServiceImpl;
import feign.FeignException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class LocationServiceTest {

    @InjectMocks
    private LocationServiceImpl locationService;

    @Mock
    private LocationDao locationDao;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private ImageDao imageDao;

    @Mock
    private S3Manager s3Manager;

    @Mock
    private PartnerClient partnerClient;

    @Test
    public void saveTest() {
        int locationId = 1;

        Location location = getLocation(locationId);

        when(locationDao.save(Mockito.any(Location.class))).thenReturn(getLocation(locationId));

        when(modelMapper.map(Mockito.any(LocationDto.class), Mockito.eq(Location.class))).
                thenReturn(getLocation(locationId));

        when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class))).
                thenReturn(getMainLocationDto(locationId));

        MainLocationDto mainlocationDto = locationService.save(getLocationDto());

        Assertions.assertEquals(location.getName(), mainlocationDto.getName());
    }

    @Test
    public void updateTest() {
        int locationId = 1;

        Location location = getLocation(locationId);
        location.setName("YogaMaster");

        LocationDto locationDto = getLocationDto();
        locationDto.setName("YogaMaster");

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        when(locationDao.getByPhoneNumber(anyString())).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(locationId));

        when(locationDao.update(Mockito.any(Location.class))).thenReturn(location);

        when(modelMapper.map(Mockito.any(LocationDto.class), Mockito.eq(Location.class)))
                .thenReturn(location);

        when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(LocationDto.class))).
                thenReturn(locationDto);

        LocationDto newLocationDto = locationService.update(getLocationDto(), locationId);

        Assertions.assertEquals(newLocationDto.getName(), "YogaMaster");
    }

    @Test
    public void updateLocationByIdIncorrectTest() {
        int incorrectLocationId = 100;

        when(locationDao.getById(incorrectLocationId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                locationService.update(getLocationDto(), incorrectLocationId));
    }

    @Test
    public void deleteTest() {
        int locationId = 1;

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        doNothing().when(locationDao).delete(Mockito.any(Location.class));

        Assertions.assertDoesNotThrow(() -> locationService.delete(locationId));
    }

    @Test
    public void deleteByIdIncorrectTest() {
        int incorrectLocationId = 100;

        when(locationDao.getById(incorrectLocationId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                locationService.delete(incorrectLocationId));
    }

    @Test
    public void getByIdTest() {
        int locationId = 1;

        Location location = getLocation(locationId);

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenReturn(getMainLocationDto(locationId));

        MainLocationDto mainLocationDto = locationService.getById(locationId);

        Assertions.assertEquals(location.getName(), mainLocationDto.getName());
    }

    @Test
    public void getByIdIncorrectTest() {
        int incorrectLocationId = 100;

        when(locationDao.getById(incorrectLocationId)).thenReturn(null);

        Assertions.assertThrows(EntityNotFoundException.class, () ->
                locationService.getById(incorrectLocationId));
    }

    @Test
    public void getByNameTest() {
        List<Location> locationList = new ArrayList<>();
        locationList.add(getLocation(1));
        locationList.add(getLocation(2));

        when(locationDao.getByName("YogaMaster")).thenReturn(locationList);

        when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenReturn(getMainLocationDto(1));

        List<MainLocationDto> mainLocationDtoList = locationService.getByName("YogaMaster");
        Assertions.assertTrue(mainLocationDtoList.size() > 0);
    }

    @Test
    public void getAllTest() {
        List<Location> locationList = new ArrayList<>();
        locationList.add(getLocation(1));
        locationList.add(getLocation(2));

        when(locationDao.getAll()).thenReturn(locationList);

        when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    Location location = invocationOnMock.getArgument(0);
                    return getMainLocationDto(location.getId());
                });

        List<MainLocationDto> mainLocationDtoList = locationService.getAll();
        Assertions.assertEquals(2, mainLocationDtoList.size());
    }

    @Test
    public void saveImageTest() throws IOException {
        int locationId = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        Image image = Image.builder().location(getLocation(locationId)).build();

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        when(imageDao.save(image))
                .then((Answer<Image>) invocationOnMock -> {
                    Image image1 = invocationOnMock.getArgument(0);
                    image1.setId(1);
                    return image1;
                });

        when(s3Manager.saveImageById(multipartFile, locationId, "locations/location_" + locationId,
                "1_photo"))
                .thenReturn("https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo");

        when(imageDao.update(Mockito.any(Image.class))).then((Answer<Image>) invocationOnMock ->
                invocationOnMock.getArgument(0));

        Mockito.when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto(1);
                    List<MainImageDto> mainImageDtos = new ArrayList<>();
                    mainImageDtos.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(mainImageDtos);
                    return mainLocationDto;
                });

        MainLocationDto mainLocationDto = locationService.saveImage(multipartFile, locationId);
        Assertions.assertEquals(mainLocationDto.getPhotos().get(0).getUrl(),
                "https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo");
    }

    @Test
    public void saveImagesTest() throws IOException {
        int locationId = 1;
        String[] urls = {"https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"};
        Image image = Image.builder().location(getLocation(locationId)).build();

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        when(imageDao.save(image))
                .then((Answer<Image>) invocationOnMock -> {
                    Image image1 = invocationOnMock.getArgument(0);
                    image1.setId(1);
                    return image1;
                });

        Mockito.when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto(1);
                    List<MainImageDto> mainImageDtos = new ArrayList<>();
                    mainImageDtos.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(mainImageDtos);
                    return mainLocationDto;
                });

        MainLocationDto mainLocationDto = locationService.saveImages(urls, locationId);
        Assertions.assertEquals(mainLocationDto.getPhotos().get(0).getUrl(),
                "https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo");
    }

    @Test
    public void deleteImageTest() {
        int locationId = 1;
        int imageId = 1;

        when(imageDao.getById(imageId)).thenReturn(ImageBuilder.getImage());

        doNothing().when(s3Manager).deleteImage(locationId, "locations/location_" + locationId,
                imageId + "_photo");

        doNothing().when(imageDao).delete(ImageBuilder.getImage());

        Assertions.assertDoesNotThrow(() -> locationService.deleteImage(locationId, imageId));
    }

    @Test
    public void deleteImageIncorrectTest() {
        int locationId = 1;
        int imageId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> locationService.deleteImage(locationId, imageId));
    }

    @Test
    public void updateImageTest() throws IOException {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        int locationId = 1;
        int imageId = 1;

        when(imageDao.getById(imageId)).thenReturn(ImageBuilder.getImage());

        when(s3Manager.saveImageById(multipartFile, locationId, "locations/location_" + locationId,
                imageId + "_photo"))
                .thenReturn("https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo");

        when(imageDao.update(Mockito.any(Image.class))).then((Answer<Image>) invocationOnMock ->
                invocationOnMock.getArgument(0));

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        Mockito.when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto(1);
                    List<MainImageDto> mainImageDtos = new ArrayList<>();
                    mainImageDtos.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(mainImageDtos);
                    return mainLocationDto;
                });

        MainLocationDto mainLocationDto = locationService.updateImage(multipartFile, locationId, imageId);
        Assertions.assertEquals(mainLocationDto.getPhotos().get(0).getUrl(),
                "https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo");
    }

    @Test
    public void updateImageIncorrectTest() throws IOException {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        int locationId = 1;
        int imageId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> locationService.updateImage(multipartFile, locationId, imageId));
    }

    @Test
    public void updateImageTest2() throws IOException {
        String url = "https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/2_photo";
        int locationId = 1;
        int imageId = 1;

        when(imageDao.getById(imageId)).thenReturn(ImageBuilder.getImage());

        doNothing().when(s3Manager).deleteImage(locationId, "locations/location_" + locationId,
                imageId + "_photo");

        when(imageDao.update(Mockito.any(Image.class))).then((Answer<Image>) invocationOnMock ->
                invocationOnMock.getArgument(0));

        when(locationDao.getById(locationId)).thenAnswer((Answer<Location>) invocationOnMock ->
                getLocation(invocationOnMock.getArgument(0)));

        Mockito.when(modelMapper.map(Mockito.any(Location.class), Mockito.eq(MainLocationDto.class)))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto(1);
                    List<MainImageDto> mainImageDtos = new ArrayList<>();
                    MainImageDto image = ImageBuilder.getMainImageDto();
                    image.setUrl(url);
                    mainImageDtos.add(image);
                    mainLocationDto.setPhotos(mainImageDtos);
                    return mainLocationDto;
                });

        MainLocationDto mainLocationDto = locationService.updateImage(url, locationId, imageId);
        Assertions.assertEquals(mainLocationDto.getPhotos().get(0).getUrl(), url);
    }

    @Test
    public void updateImageIncorrectTest2() throws IOException {
        String url = "https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/2_photo";
        int locationId = 1;
        int imageId = 1;

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> locationService.updateImage(url, locationId, imageId));
    }

    @Test
    public void userNotFoundSetAdminTest() {
        when(locationDao.getById(anyInt())).thenReturn(getLocation(1));
        when(partnerClient.getUserById(anyInt())).thenThrow(FeignException.NotFound.class);

        Assertions.assertThrows(EntityNotFoundException.class,
                () -> locationService.setAdmin(1, 1));
    }

    @Test
    public void userNotVenueAdminSetAdminTest() {
        when(locationDao.getById(anyInt())).thenReturn(getLocation(1));
        when(partnerClient.getUserById(anyInt())).thenReturn(getMainUserDto());

        Assertions.assertThrows(EntityRoleException.class,
                () -> locationService.setAdmin(1, 1));
    }

    private MainUserDto getMainUserDto() {
        Set<RoleDto> roles = new HashSet<>();
        roles.add(RoleDto.builder().id(1).name("USER").build());
        return MainUserDto.builder()
                .roles(roles)
                .build();
    }

    private LocationDto getLocationDto() {
        return LocationDto.builder()
                .name("Aquapark")
                .address(AddressDto.builder()
                        .latitude(145.4)
                        .longitude(45.2)
                        .build())
                .locationType(LocationTypeDto.builder()
                        .name("Gym")
                        .build())
                .build();
    }

    private Location getLocation(int id) {
        return Location.builder()
                .id(id)
                .name("Aquapark")
                .address(Address.builder()
                        .id(id)
                        .latitude(145.4)
                        .longitude(45.2)
                        .build())
                .locationType(LocationType.builder()
                        .id(id)
                        .name("Gym")
                        .build())
                .build();
    }

    private MainLocationDto getMainLocationDto(int id) {
        return MainLocationDto.builder()
                .id(id)
                .name("Aquapark")
                .address(AddressDto.builder()
                        .latitude(145.4)
                        .longitude(45.2)
                        .build())
                .locationType(LocationTypeDto.builder()
                        .name("Gym")
                        .build())
                .build();
    }

}
