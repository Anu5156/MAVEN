package com.anushka;

import java.util.List;

public class App {
    public static void main(String[] args) {
        GradeCalculator calc = new GradeCalculator();
        double cgpa = calc.calculateCgpa(List.of(
            new GradeCalculator.Course("DSA", 4, "O"),
            new GradeCalculator.Course("DBMS", 3, "A+"),
            new GradeCalculator.Course("Maths", 3, "A")
        ));
        System.out.println("CGPA: " + cgpa + (calc.isDistinction(cgpa) ? " (Distinction)" : ""));
    }
}
