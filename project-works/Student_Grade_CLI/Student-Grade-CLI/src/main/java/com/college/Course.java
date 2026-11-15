package com.college;

import java.util.Objects;

public record Course(String name, int credits, Grade grade) {

    public Course {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Course name is required");
        }
        if (credits <= 0) {
            throw new IllegalArgumentException("Credits must be positive: " + name);
        }
        Objects.requireNonNull(grade, "Grade is required for " + name);
    }

    public int gradePoints() {
        return credits * grade.points();
    }
}