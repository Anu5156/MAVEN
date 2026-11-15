package com.college;

import java.util.List;

public class GpaCalculator {

    public double sgpa(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            throw new IllegalArgumentException("At least one course is required");
        }
        int totalCredits = 0;
        int totalPoints = 0;
        for (Course course : courses) {
            totalCredits += course.credits();
            totalPoints += course.gradePoints();
        }
        return round2((double) totalPoints / totalCredits);
    }

    static double round2(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
