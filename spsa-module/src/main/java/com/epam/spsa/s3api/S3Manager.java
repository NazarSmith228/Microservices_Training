package com.epam.spsa.s3api;

import org.springframework.web.multipart.MultipartFile;

public interface S3Manager {

    String saveImageById(MultipartFile image, int objectId, String type);

    void deleteFileByUserId(int userId, String type);

}
