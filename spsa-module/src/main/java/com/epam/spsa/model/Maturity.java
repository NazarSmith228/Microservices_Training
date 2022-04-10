package com.epam.spsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum Maturity {

    BEGINNER("Beginner"),
    MIDDLE("Middle"),
    PRO("Pro");

    private String maturity;

    Maturity(String maturity) {
        this.maturity = maturity;
    }

    public static Maturity getFromName(String name) {
        Optional<Maturity> maturity = Arrays
                .stream(values())
                .filter(x -> x.getMaturity().equalsIgnoreCase(name))
                .findFirst();
        if (maturity.isPresent()) {
            return maturity.get();
        } else {
            throw new UnsupportedOperationException("Unsupported maturity: " + name);
        }
    }

    public static boolean isMaturity(String name) {
        Optional<Maturity> maturity = Arrays
                .stream(values())
                .filter(x -> x.getMaturity().equalsIgnoreCase(name))
                .findFirst();
        return maturity.isPresent();
    }

}
