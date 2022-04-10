package com.epam.slsa.controller;

import com.epam.slsa.builders.locationType.LocationTypeBuilder;
import com.epam.slsa.builders.photo.FileBuilder;
import com.epam.slsa.builders.photo.ImageBuilder;
import com.epam.slsa.builders.sportType.SportTypeBuilder;
import com.epam.slsa.dao.LocationTypeDao;
import com.epam.slsa.dao.SportTypeDao;
import com.epam.slsa.dto.image.MainImageDto;
import com.epam.slsa.dto.location.LocationDto;
import com.epam.slsa.dto.location.MainLocationDto;
import com.epam.slsa.dto.locationType.LocationTypeDto;
import com.epam.slsa.error.exception.EntityNotFoundException;
import com.epam.slsa.model.LocationType;
import com.epam.slsa.model.SportType;
import com.epam.slsa.service.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.mockito.stubbing.Answer;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.epam.slsa.builders.location.LocationDtoBuilder.getLocationDto;
import static com.epam.slsa.builders.location.LocationDtoBuilder.getMainLocationDto;
import static com.epam.slsa.builders.location.LocationInfo.locationId;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LocationController.class)
@PropertySource("classpath:/messages.properties")
@AutoConfigureMockMvc(addFilters = false)
public class LocationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LocationService locationService;
    @MockBean
    private LocationTypeDao locationTypeDao;

    @MockBean
    private SportTypeDao sportTypeDao;

    @MockBean
    private ModelMapper modelMapper;

    @Value("${location.exception.notfound}")
    private String locationExceptionMessage;

    private SportType expectedSportType;
    private LocationType expectedLocationType;
    private List<SportType> sportTypeList;
    private List<LocationType> locationTypeList;
    private MainLocationDto expectedLocationDto;


    @BeforeEach
    public void setUpVariables() {
        expectedSportType = SportTypeBuilder.getSportType();

        expectedLocationType = LocationTypeBuilder.getLocationType();

        sportTypeList = Lists.newArrayList(expectedSportType);
        locationTypeList = Lists.newArrayList(expectedLocationType);
        expectedLocationDto = getMainLocationDto();

    }

    @BeforeEach
    public void setUpMockito() throws Exception {
        Mockito.when(modelMapper.map(Mockito.anySet(), Mockito.any(Type.class)))
                .thenReturn(Lists.newArrayList(expectedSportType));

        Mockito.when(modelMapper.map(Mockito.any(LocationTypeDto.class), Mockito.any()))
                .thenReturn(expectedLocationType);

        Mockito.when(locationTypeDao.getAll())
                .thenReturn(locationTypeList);

        Mockito.when(sportTypeDao.getAll())
                .thenReturn(sportTypeList);

        Mockito.when(locationService.save(Mockito.any(LocationDto.class)))
                .thenReturn(getMainLocationDto());

        Mockito.when(locationService.update(Mockito.any(LocationDto.class), Mockito.anyInt()))
                .thenReturn(getLocationDto());

        Mockito.when(locationService.getAll()).
                thenReturn(Lists.list(expectedLocationDto));

        Mockito.when(locationService.getById(ArgumentMatchers.eq(locationId)))
                .thenReturn(getMainLocationDto());

        Mockito.when(locationService.getById(ArgumentMatchers.eq(-1)))
                .thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void getAll() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void getByLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}", locationId)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void getByIncorrectLocationId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/locations/{id}", -1)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveLocation() throws Exception {
        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getLocationDto())))
                .andExpect(jsonPath("$.name").value("Fun club"))
                .andExpect(jsonPath("$.address.latitude").value(12.5))
                .andExpect(jsonPath("$.address.longitude").value(120.0))
                .andExpect(jsonPath("$.locationType.id").value(4))
                .andExpect(jsonPath("$.locationType.name").value("Studio"))
                .andExpect(jsonPath("$.sportTypes.[0].id").value(2))
                .andExpect(jsonPath("$.sportTypes.[0].name").value("Football"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testSaveVoidLocation() throws Exception {
        Mockito.when(locationService.save(ArgumentMatchers.any(LocationDto.class)))
                .thenReturn(getMainLocationDto());

        mockMvc.perform(post("/api/v1/locations")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testUpdateLocation() throws Exception {
        Mockito.when(locationService.update(Mockito.any(LocationDto.class), Mockito.anyInt()))
                .thenReturn(getLocationDto());

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/locations/{id}", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getLocationDto())))
                .andExpect(jsonPath("$.name").value("Fun club"))
                .andExpect(jsonPath("$.address.latitude").value(120.0))
                .andExpect(jsonPath("$.address.longitude").value(12.5))
                .andExpect(jsonPath("$.locationType.id").value(4))
                .andExpect(jsonPath("$.locationType.name").value("Studio"))
                .andExpect(jsonPath("$.sportTypes.[0].id").value(2))
                .andExpect(jsonPath("$.sportTypes.[0].name").value("Football"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteLocation() throws Exception {
        int locationId = 3;

        doNothing()
                .when(locationService)
                .delete(locationId);

        mockMvc.perform(delete("/api/v1/locations/{id}", locationId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(getLocationDto())))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteExceptionCoach() throws Exception {
        int locationId = -3;

        doThrow(new EntityNotFoundException(locationExceptionMessage + locationId))
                .when(locationService)
                .delete(locationId);

        mockMvc.perform(delete("/api/v1/locations/{id}", locationId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(locationExceptionMessage + locationId))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testSaveImage() throws Exception {
        int locationId = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(locationService.saveImage(multipartFile, locationId))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto();
                    List<MainImageDto> imageList = new ArrayList<>();
                    imageList.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(imageList);
                    return mainLocationDto;
                });

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/locations/{id}/images", locationId)
                .file(multipartFile))
                .andExpect(jsonPath("$.photos.[0].id").value(1))
                .andExpect(jsonPath("$.photos.[0].url").value("https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"))
                .andExpect(status().isCreated());
    }

    @Test
    public void testIncorrectSaveImage() throws Exception {
        int locationId = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "txtTest.txt", "txt/plane", FileBuilder.getTxtBytes());

        Mockito.when(locationService.saveImage(multipartFile, locationId))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto();
                    List<MainImageDto> imageList = new ArrayList<>();
                    imageList.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(imageList);
                    return mainLocationDto;
                });

        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/v1/locations/{id}/images", locationId)
                .file(multipartFile))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testSaveImageUrl() throws Exception {
        int locationId = 1;
        String[] url = {"https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"};

        Mockito.when(locationService.saveImages(url, locationId))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto();
                    List<MainImageDto> imageList = new ArrayList<>();
                    imageList.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(imageList);
                    return mainLocationDto;
                });

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/locations/{id}/url-images", locationId)
                .param("url", url))
                .andExpect(jsonPath("$.photos.[0].id").value(1))
                .andExpect(jsonPath("$.photos.[0].url").value("https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeleteImage() throws Exception {
        int locationId = 1;
        int imageId = 1;
        Mockito.doNothing().when(locationService).deleteImage(locationId, imageId);

        mockMvc.perform(delete("/api/v1/locations/{locationId}/images/{imageId}", locationId, imageId))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateImage() throws Exception {
        int locationId = 1;
        int imageId = 1;
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());

        Mockito.when(locationService.updateImage(multipartFile, locationId, imageId))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto();
                    List<MainImageDto> imageList = new ArrayList<>();
                    imageList.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(imageList);
                    return mainLocationDto;
                });

        MockMultipartHttpServletRequestBuilder builder =
                MockMvcRequestBuilders.multipart("/api/v1/locations/{locationId}/images/{imageId}", locationId, imageId);

        builder.with(request -> {
            request.setMethod("PUT");
            return request;
        });

        mockMvc.perform(builder
                .file(multipartFile))
                .andExpect(jsonPath("$.photos.[0].id").value(1))
                .andExpect(jsonPath("$.photos.[0].url").value("https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUpdateUrlImage() throws Exception {
        int locationId = 1;
        int imageId = 1;
        String[] url = {"https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"};

        Mockito.when(locationService.updateImage(url[0], locationId, imageId))
                .thenAnswer((Answer<MainLocationDto>) invocationOnMock -> {
                    MainLocationDto mainLocationDto = getMainLocationDto();
                    List<MainImageDto> imageList = new ArrayList<>();
                    imageList.add(ImageBuilder.getMainImageDto());
                    mainLocationDto.setPhotos(imageList);
                    return mainLocationDto;
                });

        mockMvc.perform(put("/api/v1/locations/{locationId}/url-images/{imageId}", locationId, imageId)
                .param("url", url))
                .andExpect(jsonPath("$.photos.[0].id").value(1))
                .andExpect(jsonPath("$.photos.[0].url").value("https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo"))
                .andExpect(status().isOk());
    }

}
