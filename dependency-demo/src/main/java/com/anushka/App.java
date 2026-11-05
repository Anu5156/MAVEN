package com.anushka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import java.util.List;

public class App {
    record Student(String name, List<String> dreamCompanies) {}

    public static void main(String[] args) throws Exception {
        Student me = new Student("Anushka", ImmutableList.of("Google", "Amazon", "Microsoft"));
        String json = new ObjectMapper().writeValueAsString(me);
        System.out.println(json);
    }
}