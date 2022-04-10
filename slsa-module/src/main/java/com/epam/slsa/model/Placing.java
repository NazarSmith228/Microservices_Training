package com.epam.slsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Placing {

    INDOOR("Indoor"),
    OUTDOOR("Outdoor"),
    ANY("Any");

    private String name;

    Placing(String name) {
        this.name = name;
    }

    public static Placing getFromName(String name) {
        Optional<Placing> placing = Arrays
                .stream(values())
                .filter(p -> p.getName().equalsIgnoreCase(name))
                .findFirst();
        if (placing.isPresent()) {
            return placing.get();
        } else if (name == null) {
            return ANY;
        } else {
            throw new UnsupportedOperationException(
                    "The name " + name + " is not supported!"
            );
        }
    }


    public static Boolean isValidPlacing(String name) {
        Optional<Placing> placing =
                Arrays.stream(values())
                        .filter(d -> d.getName().equalsIgnoreCase(name)).findFirst();
        return (placing.isPresent() || name == null);
    }

}
