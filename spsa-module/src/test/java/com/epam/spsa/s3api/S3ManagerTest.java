package com.epam.spsa.s3api;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.stubbing.Answer;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayInputStream;
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
        s3Manager.setRegion("eu-north-1");
        s3Manager.setBucketName("spsaphoto");
    }

    @Test
    public void saveImageByIdTest() throws IOException {
        int userId = 1;
        MockMultipartFile image =
                new MockMultipartFile("file", "pngTest.png", "image/png", PhotoBuilder.getPngBytes());
        String type = "user";

        PutObjectRequest request =
                new PutObjectRequest("spsaphoto/" + type, userId + "_photo", image.getInputStream(), new ObjectMetadata());

        Mockito.doNothing().when(s3Manager).initClient();
        Mockito.when(s3Client.putObject(request)).thenReturn(new PutObjectResult());

        String url = s3Manager.saveImageById(image, userId, type);
        Assertions.assertEquals("https://spsaphoto.s3.eu-north-1.amazonaws.com/" + type + "/" + userId + "_photo",
                url);
    }

    @Test
    public void deleteFileByUserId() {
        int id = 1;
        String type = "user";
        DeleteObjectRequest deleteObjectRequest
                = new DeleteObjectRequest("spsaphoto/" + type, id + "_photo");

        Mockito.doNothing().when(s3Manager).initClient();

        Mockito.doNothing().when(s3Client).deleteObject(deleteObjectRequest);

        Assertions.assertDoesNotThrow(() -> s3Manager.deleteFileByUserId(id, type));

    }

}