package com.epam.slsa.s3api;

import org.springframework.web.multipart.MultipartFile;

public interface S3Manager {

    String saveImageById(MultipartFile image, int locationId, String type, String fileName);

    void deleteImage(int locationId, String type, String fileName);

}
