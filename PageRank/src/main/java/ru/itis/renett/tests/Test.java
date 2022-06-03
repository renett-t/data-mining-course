package ru.itis.renett.tests;

import ru.itis.renett.csv.CSVWriter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import static ru.itis.renett.csv.CSVWriter.TRASH;

public class Test {
    public static String LINK = "https://www.instagram.com/renett_t/";

    public static void main(String[] args) throws IOException {
        Set<String> links = new HashSet<>();
        links.add("sidfjsf");
        links.add("jfssfdk");
        links.add("tyyyyyyyyyyyy");

        CSVWriter.writeLibrary(links, TRASH);
    }
}
