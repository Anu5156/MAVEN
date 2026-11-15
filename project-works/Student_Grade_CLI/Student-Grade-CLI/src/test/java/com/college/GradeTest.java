package com.college;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GradeTest {

    @ParameterizedTest(name = "{0} = {1} points")
    @CsvSource({"O,10", "A+,9", "A,8", "B+,7", "B,6", "C,5", "P,4", "F,0"})
    void labelsMapToPoints(String label, int points) {
        assertEquals(points, Grade.fromLabel(label).points());
    }

    @Test
    void lowercaseAndSpacesAreAccepted() {
        assertEquals(Grade.A_PLUS, Grade.fromLabel(" a+ "));
    }

    @Test
    void unknownGradeThrows() {
        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel("Z"));
        assertEquals("Unknown grade: Z", ex.getMessage());
    }

    @Test
    void nullGradeThrows() {
        assertThrows(IllegalArgumentException.class, () -> Grade.fromLabel(null));
    }
}