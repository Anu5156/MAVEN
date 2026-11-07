package com.anushka;

import static org.junit.jupiter.api.Assertions.*;

import com.anushka.GradeCalculator.Course;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class GradeCalculatorTest {

    private final GradeCalculator calc = new GradeCalculator();

    @ParameterizedTest(name = "grade {0} = {1} points")
    @CsvSource({"O,10", "A+,9", "A,8", "B+,7", "B,6", "C,5", "P,4", "F,0"})
    void gradePoints(String grade, int expected) {
        assertEquals(expected, calc.gradePoint(grade));
    }

    @Test
    @DisplayName("CGPA is the credit-weighted average")
    void cgpaIsWeightedAverage() {
        List<Course> courses = List.of(
            new Course("DSA", 4, "O"),     // 4 x 10 = 40
            new Course("DBMS", 3, "A+"),   // 3 x 9  = 27
            new Course("Maths", 3, "A")    // 3 x 8  = 24
        );                                 // 91 / 10 = 9.1
        assertEquals(9.1, calc.calculateCgpa(courses), 0.001);

    }
        @Test
    void nullGradeThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.gradePoint(null));
    }

    @Test
    void unknownGradeThrowsWithMessage() {
        IllegalArgumentException ex =
            assertThrows(IllegalArgumentException.class, () -> calc.gradePoint("Z"));
        assertEquals("Unknown grade: Z", ex.getMessage());
    }

    @Test
    void lowercaseAndSpacesAreAccepted() {
        assertEquals(9, calc.gradePoint(" a+ "));
    }

    @Test
    void emptyCourseListThrows() {
        assertThrows(IllegalArgumentException.class, () -> calc.calculateCgpa(List.of()));
    }

    @Test
    void nonPositiveCreditsThrow() {
        List<Course> bad = List.of(new Course("Lab", 0, "O"));
        assertThrows(IllegalArgumentException.class, () -> calc.calculateCgpa(bad));
    }

    @ParameterizedTest(name = "cgpa {0} → distinction = {1}")
    @CsvSource({"9.1,true", "8.5,true", "8.49,false"})
    void distinction(double cgpa, boolean expected) {
        assertEquals(expected, calc.isDistinction(cgpa));
    }
}