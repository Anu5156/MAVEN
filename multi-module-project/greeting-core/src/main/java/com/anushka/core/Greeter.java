package com.anushka.core;

public class Greeter {
    public String greet(String name) {
        if (name == null || name.isBlank()) {
            return "Hello, stranger!";
        }
        return "Hello, " + name + "!";
    }
}