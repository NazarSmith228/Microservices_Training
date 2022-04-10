package com.epam.slsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Gender {

    MALE("Male"),
    FEMALE("Female"),
    OTHER("Other");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public static Gender fromName(String name) {
        Optional<Gender> gender = Arrays.stream(values()).filter(d -> d.getName().toLowerCase().equals(name.toLowerCase())).findFirst();
        if (gender.isPresent()) {
            return gender.get();
        } else {
            throw new UnsupportedOperationException(
                    "The name " + name + " is not supported!"
            );
        }
    }

    public static Boolean isValidGender(String name) {
        Optional<Gender> gender = Arrays.stream(values()).filter(d -> d.getName().toLowerCase().equals(name.toLowerCase())).findFirst();
        return gender.isPresent();
    }

}
