package com.pet.fitnesstracker.domain.enumeration;

import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * @author Elmo Lingad
 */
public enum Intensity {
    LOW("low"),
    MEDIUM("medium"),
    HIGH("high");

    private final String value;

    Intensity(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static boolean isValidIntensity(String intensity) {
        return Arrays.stream(Intensity.values())
            .map(Intensity::getValue)
            .collect(Collectors.toSet())
            .contains(intensity.toLowerCase());
    }
}
