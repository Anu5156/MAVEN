package com.college;

import java.util.List;

public class App {
    public static void main(String[] args) {
        GpaCalculator calculator = new GpaCalculator();

        List<Course> semester = List.of(
            new Course("Data Structures", 4, Grade.O),
            new Course("DBMS", 3, Grade.A_PLUS),
            new Course("Operating Systems", 3, Grade.A),
            new Course("Maths", 4, Grade.B_PLUS),
            new Course("Java Lab", 2, Grade.O)
        );

        System.out.println("===== Semester Report =====");
        for (Course c : semester) {
            System.out.printf("%-20s %d credits   %-3s -> %2d points%n",
                c.name(), c.credits(), c.grade(), c.gradePoints());
        }
        System.out.println("---------------------------");
        System.out.println("SGPA: " + calculator.sgpa(semester));
    }
}