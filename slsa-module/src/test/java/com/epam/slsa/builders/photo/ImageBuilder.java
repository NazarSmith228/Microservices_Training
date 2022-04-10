package com.epam.slsa.builders.photo;

import com.epam.slsa.dto.image.MainImageDto;
import com.epam.slsa.model.Image;

public class ImageBuilder {

    public static Image getImage() {
        return Image.builder()
                .id(ImageInfo.id)
                .url(ImageInfo.url)
                .build();
    }

    public static MainImageDto getMainImageDto() {
        return MainImageDto.builder()
                .id(ImageInfo.id)
                .url(ImageInfo.url)
                .build();
    }
}
