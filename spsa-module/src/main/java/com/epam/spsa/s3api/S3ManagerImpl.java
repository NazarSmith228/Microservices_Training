package com.epam.spsa.s3api;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@PropertySource("classpath:s3.properties")
@Setter
@Slf4j
public class S3ManagerImpl implements S3Manager {

    @Value("${awsCreds.accessKey}")
    private String accessKey;

    @Value("${awsCreds.secretKey}")
    private String secretKey;

    @Value("${aws.bucket}")
    private String bucketName;

    @Value("${aws.region}")
    private String region;

    private AmazonS3 s3client;

    @Override
    public String saveImageById(MultipartFile image, int objectId, String type) {
        log.info("Saving " + type + "'s image by " + type + "'s id:{}", objectId);
        initClient();

        PutObjectRequest request = null;
        try {
            request = new PutObjectRequest(bucketName + "/" + type,
                    objectId + "_photo", image.getInputStream(), new ObjectMetadata());
        } catch (Exception e) {
            log.info("Error of input image to S3");
            e.printStackTrace();
        }

        s3client.putObject(request);
        return "https://" + bucketName + ".s3." + region +
                ".amazonaws.com/" + type + "/" + objectId + "_photo";
    }

    public void deleteFileByUserId(int id, String type) {
        log.info("Deleting " + type + "'s image by " + type + "'s id:{}", id);
        initClient();
        DeleteObjectRequest deleteObjectRequest
                = new DeleteObjectRequest(bucketName + "/" + type, id + "_photo");
        s3client.deleteObject(deleteObjectRequest);
    }

    public void initClient() {
        log.info("Init AWS client");
        BasicAWSCredentials awsCreds =
                new BasicAWSCredentials(accessKey, secretKey);

        s3client = AmazonS3ClientBuilder.standard()
                .withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
                .build();
    }

}
