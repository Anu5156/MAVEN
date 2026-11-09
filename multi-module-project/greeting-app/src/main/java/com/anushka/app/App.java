package com.anushka.app;

import com.anushka.core.Greeter;

public class App {
    public static void main(String[] args) {
        Greeter greeter = new Greeter();
        System.out.println(greeter.greet("Anushka"));
        System.out.println(greeter.greet(""));
    }
}