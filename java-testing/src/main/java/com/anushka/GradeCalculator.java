package com.anushka;

import java.util.List;

public class GradeCalculator {

    public record Course(String name, int credits, String grade) {}

    public int gradePoint(String grade) {
        if (grade == null) {
            throw new IllegalArgumentException("Grade must not be null");
        }
        return switch (grade.trim().toUpperCase()) {
            case "O" -> 10;
            case "A+" -> 9;
            case "A" -> 8;
            case "B+" -> 7;
            case "B" -> 6;
            case "C" -> 5;
            case "P" -> 4;
            case "F" -> 0;
            default -> throw new IllegalArgumentException("Unknown grade: " + grade);
        };
    }

    public double calculateCgpa(List<Course> courses) {
        if (courses == null || courses.isEmpty()) {
            throw new IllegalArgumentException("At least one course is required");
        }
        int totalCredits = 0;
        int weightedPoints = 0;
        for (Course c : courses) {
            if (c.credits() <= 0) {
                throw new IllegalArgumentException("Credits must be positive: " + c.name());
            }
            totalCredits += c.credits();
            weightedPoints += c.credits() * gradePoint(c.grade());
        }
        double cgpa = (double) weightedPoints / totalCredits;
        return Math.round(cgpa * 100.0) / 100.0;
    }

    public boolean isDistinction(double cgpa) {
        return cgpa >= 8.5;
    }
}