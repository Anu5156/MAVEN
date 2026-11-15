package com.college;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import org.junit.jupiter.api.Test;

class GpaCalculatorTest {

    private final GpaCalculator calculator = new GpaCalculator();

    @Test
    void sgpaIsCreditWeightedAverage() {
        List<Course> sem = List.of(
            new Course("DSA", 4, Grade.O),        // 40
            new Course("DBMS", 3, Grade.A_PLUS),  // 27
            new Course("Maths", 3, Grade.A)       // 24
        );                                        // 91 / 10 = 9.1
        assertEquals(9.1, calculator.sgpa(sem), 0.001);
    }

    @Test
    void sgpaIsRoundedToTwoDecimals() {
        List<Course> sem = List.of(
            new Course("Data Structures", 4, Grade.O),
            new Course("DBMS", 3, Grade.A_PLUS),
            new Course("Operating Systems", 3, Grade.A),
            new Course("Maths", 4, Grade.B_PLUS),
            new Course("Java Lab", 2, Grade.O)
        );                                        // 139 / 16 = 8.6875 → 8.69
        assertEquals(8.69, calculator.sgpa(sem), 0.001);
    }

    @Test
    void emptySemesterThrows() {
        assertThrows(IllegalArgumentException.class, () -> calculator.sgpa(List.of()));
    }
}