package ru.renett.services.crawl.serialization;

import org.springframework.stereotype.Component;
import ru.renett.exceptions.FileReaderException;

import java.io.BufferedReader;
import java.io.IOException;

@Component
public class SimpleFileReader {

    public String readLinesFromFile(String fileName) throws FileReaderException {
       StringBuilder builder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }
        } catch (IOException e) {
            System.out.println("SOS CAN'T READ, reason: " + e.getMessage());
            throw new FileReaderException(e);
        }
        return builder.toString();
    }

}
