package com.epam.spsa.validation.validators;

import com.epam.spsa.validation.Image;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;


@Slf4j
public class FileValidator implements ConstraintValidator<Image, MultipartFile> {

    private final String[] listOfFormats = {"image/png", "image/jpeg"};

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        log.info("Checking if file is image");
        if (!(Objects.equals(multipartFile.getContentType(), listOfFormats[0])
                || Objects.equals(multipartFile.getContentType(), listOfFormats[1]))) {
            log.error("File is not image, it's = " + multipartFile.getContentType());
            return false;
        }

        log.info("Checking if file has allowed sizes");
        int height = 0;
        int width = 0;

        try {
            BufferedImage bufferedImage = ImageIO.read(multipartFile.getInputStream());
            height = bufferedImage.getHeight();
            width = bufferedImage.getWidth();
        } catch (IOException e) {
            log.error("Throwing IOException");
            e.printStackTrace();
        }

        int heightLimit = 1000000000;
        int widthLimit = 1000000000;
        return height <= heightLimit && width <= widthLimit;
    }

}
