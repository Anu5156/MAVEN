package com.anushka;

import org.apache.commons.lang3.StringUtils;

public class App {
    public static void main(String[] args) {
        String dream = "google";
        System.out.println("Capitalized: " + StringUtils.capitalize(dream));
        System.out.println("Reversed:    " + StringUtils.reverse(dream));
        System.out.println("Is blank?    " + StringUtils.isBlank("   "));
    }
}