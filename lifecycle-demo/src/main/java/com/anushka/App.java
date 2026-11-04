package com.anushka;

public class App {
    public static void main(String[] args) {
        Calculator calc = new Calculator();
        System.out.println("2 + 3 = " + calc.add(2, 3));
        System.out.println("6 / 3 = " + calc.divide(6, 3));
    }
}