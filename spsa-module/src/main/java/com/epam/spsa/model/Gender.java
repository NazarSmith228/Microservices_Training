package com.epam.spsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Gender {

    MALE("Male"),
    FEMALE("Female"),
    BOTH("Both");

    private String gender;

    Gender(String gender) {
        this.gender = gender;
    }

    public static Gender getFromName(String name) {
        Optional<Gender> gender = Arrays
                .stream(values())
                .filter(x -> x.getGender().equalsIgnoreCase(name))
                .findFirst();
        if (gender.isPresent()) {
            return gender.get();
        } else {
            throw new UnsupportedOperationException("Unsupported gender: " + name);
        }
    }

    public static Boolean isGender(String name) {
        Optional<Gender> gender = Arrays
                .stream(values())
                .filter(x -> x.getGender().equalsIgnoreCase(name))
                .findFirst();
        return gender.isPresent();
    }

}
