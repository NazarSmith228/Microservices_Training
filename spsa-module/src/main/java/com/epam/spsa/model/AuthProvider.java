package com.epam.spsa.model;

import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@Getter
public enum AuthProvider {

    LOCAL("Local"),
    GOOGLE("Google"),
    FACEBOOK("Facebook");

    private String name;

    AuthProvider(String name) {
        this.name = name;
    }

    public static AuthProvider fromName(String name) {
        Optional<AuthProvider> authProvider = Arrays.stream(values())
                .filter(ap -> ap.getName().toLowerCase().equals(name.toLowerCase()))
                .findFirst();
        if (authProvider.isPresent()) {
            return authProvider.get();
        } else {
            throw new UnsupportedOperationException(
                    "The name " + name + " is not supported!"
            );
        }
    }

}
