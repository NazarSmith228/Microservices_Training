package com.epam.slsa.s3api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.epam.slsa.builders.photo.FileBuilder;
import com.epam.slsa.s3api.impl.S3ManagerImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

@ExtendWith(SpringExtension.class)
public class S3ManagerTest {

    @Spy
    @InjectMocks
    private S3ManagerImpl s3Manager;

    @Mock
    private AmazonS3 s3Client;

    @BeforeEach
    public void init() {
        s3Manager.setBucketName("slsaphoto");
        s3Manager.setRegion("eu-north-1");
    }

    @Test
    public void saveImageByIdTest() throws IOException {
        String fileName = "1_photo";
        int locationId = 1;
        MockMultipartFile image =
                new MockMultipartFile("file", "pngTest.png", "image/png", FileBuilder.getPngBytes());
        PutObjectRequest request =
                new PutObjectRequest("slsaphoto/locations/location_" + locationId, fileName, image.getInputStream(), new ObjectMetadata());

        Mockito.doNothing().when(s3Manager).initClient();
        Mockito.when(s3Client.putObject(request)).thenReturn(new PutObjectResult());

        String objectUrl = s3Manager.saveImageById(image, locationId,
                "locations/location_" + locationId, "1_photo");
        Assertions.assertEquals(objectUrl,
                "https://slsaphoto.s3.eu-north-1.amazonaws.com/locations/location_1/1_photo");
    }

    @Test
    public void deleteImageTest() {
        String fileName = "1_photo";
        int locationId = 1;

        Mockito.doNothing().when(s3Manager).initClient();

        DeleteObjectRequest deleteObjectRequest
                = new DeleteObjectRequest("slsaphoto/locations/location_" + locationId, fileName);
        Mockito.doNothing().when(s3Client).deleteObject(deleteObjectRequest);

        Assertions.assertDoesNotThrow(() -> s3Manager.deleteImage(locationId,
                "locations/location_" + locationId, fileName));
    }

}
