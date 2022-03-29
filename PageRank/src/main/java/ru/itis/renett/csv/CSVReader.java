package ru.itis.renett.csv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CSVReader {

    public static Map<String, Integer> readLibrary(String filename) {
        ClassLoader loader = ClassLoader.getSystemClassLoader();
        InputStream stream = loader.getResourceAsStream(filename);
        Map<String, Integer> map = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(stream))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] l = line.split(",");
                map.put(l[1], Integer.parseInt(l[0]));
            }
            return map;
        } catch (IOException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }
}
