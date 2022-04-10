package com.epam.slsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum LinkType {

    YOUTUBE("Youtube"),
    INSTAGRAM("Instagram"),
    FACEBOOK("Facebook");

    private String type;

    LinkType(String type) {
        this.type = type;
    }

    public static LinkType fromName(String type) {
        Optional<LinkType> linkType = Arrays
                .stream(values())
                .filter(d -> d.getType().toLowerCase().equals(type.toLowerCase()))
                .findFirst();
        if (linkType.isPresent()) {
            return linkType.get();
        } else {
            throw new UnsupportedOperationException(
                    "The type " + linkType + " is not supported!"
            );
        }
    }

    public static Boolean isValidLinkType(String type) {
        Optional<LinkType> linkType = Arrays
                .stream(values())
                .filter(d -> d.getType().toLowerCase().equals(type.toLowerCase()))
                .findFirst();

        return linkType.isPresent();
    }

}
