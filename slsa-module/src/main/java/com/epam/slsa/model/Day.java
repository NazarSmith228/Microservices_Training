package com.epam.slsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Day {

    MONDAY("Monday"),
    TUESDAY("Tuesday"),
    WEDNESDAY("Wednesday"),
    THURSDAY("Thursday"),
    FRIDAY("Friday"),
    SATURDAY("Saturday"),
    SUNDAY("Sunday");

    private String name;

    Day(String name) {
        this.name = name;
    }

    public static Day fromName(String name) {
        Optional<Day> day = Arrays
                .stream(values())
                .filter(d -> d.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst();
        if (day.isPresent()) {
            return day.get();
        } else {
            throw new UnsupportedOperationException(
                    "The name " + name + " is not supported!"
            );
        }
    }

    public static Boolean isValidDay(String name) {
        Optional<Day> day = Arrays
                .stream(values())
                .filter(d -> d.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst();

        return day.isPresent();
    }

}
