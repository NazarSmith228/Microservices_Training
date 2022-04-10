package com.epam.spsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum ActivityTime {

    MORNING("Morning"),
    NOON("Noon"),
    EVENING("Evening"),
    ALL("All");

    private String dayPart;

    ActivityTime(String dayPart) {
        this.dayPart = dayPart;
    }

    public static ActivityTime getFromName(String name) {
        Optional<ActivityTime> dayTime = Arrays
                .stream(values())
                .filter(x -> x.getDayPart().equalsIgnoreCase(name))
                .findFirst();
        if (dayTime.isPresent()) {
            return dayTime.get();
        } else {
            throw new UnsupportedOperationException("Unsupported time: " + name);
        }
    }

    public static boolean isActivityTime(String name) {
        return Arrays
                .stream(values())
                .anyMatch(dt -> name.trim().equalsIgnoreCase(dt.getDayPart()));
    }

}
