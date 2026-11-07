package com.anushka;

import static org.junit.jupiter.api.Assertions.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import org.junit.jupiter.api.Test;

class GradeCalculatorIT {

    @Test
    void calculatesCgpaFromSemesterFile() throws Exception {
        InputStream in = getClass().getResourceAsStream("/semester1.csv");
        assertNotNull(in, "semester1.csv not found on the test classpath");

        List<GradeCalculator.Course> courses;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            courses = reader.lines()
                .skip(1)                                   // skip the header row
                .map(line -> line.split(","))
                .map(p -> new GradeCalculator.Course(p[0].trim(), Integer.parseInt(p[1].trim()), p[2].trim()))
                .toList();
        }

        assertEquals(5, courses.size());
        // (40 + 27 + 24 + 28 + 20) / 16 = 8.6875 → 8.69
        assertEquals(8.69, new GradeCalculator().calculateCgpa(courses), 0.001);
    }
}