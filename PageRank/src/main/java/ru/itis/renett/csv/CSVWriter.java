package ru.itis.renett.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CSVWriter {
    public static String LIBRARY = "library.csv";
    public static String TRASH = "trash.csv";
    public static String EMPTY = "empty.csv";
    public static String MATRIX = "matrix.csv";

    public static void writeLibrary(Set<String> values, String fileName) throws IOException {
        List<String[]> dataLines = new ArrayList<>();

        int i = 0;
        for (String value : values) {
            dataLines.add(new String[]{String.valueOf(i++), value});
        }
        givenDataArray_whenConvertToCSV_thenOutputCreated(dataLines, fileName);
    }

    public static void givenDataArray_whenConvertToCSV_thenOutputCreated(List<String[]> dataLines, String fileName) throws IOException {
        File csvOutputFile = new File(fileName);
        try (PrintWriter pw = new PrintWriter(new FileWriter(csvOutputFile))) {
            dataLines.stream()
                    .map(CSVWriter::convertToCSV)
                    .forEach(pw::println);
        }
    }

    public static String convertToCSV(String[] data) {
        return String.join(",", data);
    }

    // todo : realization of writing matrix to scv
    public static void writeMatrix(double[][] matrix, String filename) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(filename))) {
        }
    }
}
