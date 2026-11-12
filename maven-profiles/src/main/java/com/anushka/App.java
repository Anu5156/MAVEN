package com.anushka;

import java.io.InputStream;
import java.util.Properties;

public class App {
    public static void main(String[] args) throws Exception {
        Properties props = new Properties();
        try (InputStream in = App.class.getResourceAsStream("/app.properties")) {
            props.load(in);
        }
        System.out.println("Environment : " + props.getProperty("app.env"));
        System.out.println("Message     : " + props.getProperty("app.greeting"));
        System.out.println("Version     : " + props.getProperty("app.version"));
    }
}