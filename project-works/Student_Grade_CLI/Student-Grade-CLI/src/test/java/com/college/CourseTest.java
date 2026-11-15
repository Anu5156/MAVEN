package com.college;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class CourseTest {

    @Test
    void gradePointsAreCreditsTimesPoints() {
        Course dsa = new Course("Data Structures", 4, Grade.O);
        assertEquals(40, dsa.gradePoints());   // 4 x 10
    }

    @Test
    void zeroCreditsAreRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Course("Lab", 0, Grade.O));
    }

    @Test
    void blankNameIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> new Course("  ", 3, Grade.A));
    }

    @Test
    void missingGradeIsRejected() {
        assertThrows(NullPointerException.class, () -> new Course("Maths", 3, null));
    }
}