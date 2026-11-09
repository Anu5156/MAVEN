package com.anushka.core;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

class GreeterTest {

    private final Greeter greeter = new Greeter();

    @Test
    void greetsByName() {
        assertEquals("Hello, Anushka!", greeter.greet("Anushka"));
    }

    @Test
    void blankNameIsStranger() {
        assertEquals("Hello, stranger!", greeter.greet("   "));
    }
}