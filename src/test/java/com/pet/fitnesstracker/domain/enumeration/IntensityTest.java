package com.pet.fitnesstracker.domain.enumeration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

/**
 * @author Elmo Lingad
 */
class IntensityTest {

    @Test
    void checkValidValues() {
        assertEquals("low", Intensity.LOW.getValue());
        assertEquals("medium", Intensity.MEDIUM.getValue());
        assertEquals("high", Intensity.HIGH.getValue());
    }

    @Test
    void checkForIntensityValidity() {
        assertTrue(Intensity.isValidIntensity("LoW"));
        assertTrue(Intensity.isValidIntensity("MEDIUM"));
        assertTrue(Intensity.isValidIntensity("high"));

        assertFalse(Intensity.isValidIntensity("intense"));
    }

}
