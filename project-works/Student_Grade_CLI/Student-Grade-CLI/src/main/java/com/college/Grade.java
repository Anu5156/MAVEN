package com.college;

public enum Grade {
    O(10), A_PLUS(9), A(8), B_PLUS(7), B(6), C(5), P(4), F(0);

    private final int points;

    Grade(int points) {
        this.points = points;
    }

    public int points() {
        return points;
    }

    public static Grade fromLabel(String label) {
        if (label == null) {
            throw new IllegalArgumentException("Grade must not be null");
        }
        return switch (label.trim().toUpperCase()) {
            case "O" -> O;
            case "A+" -> A_PLUS;
            case "A" -> A;
            case "B+" -> B_PLUS;
            case "B" -> B;
            case "C" -> C;
            case "P" -> P;
            case "F" -> F;
            default -> throw new IllegalArgumentException("Unknown grade: " + label.trim());
        };
    }
}
