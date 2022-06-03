package ru.renett.services.crawl.serialization;

import org.springframework.stereotype.Component;
import ru.renett.exceptions.FileWriterException;

import java.io.BufferedWriter;
import java.io.IOException;

@Component
public class SimpleFileWriter {

    public void writeToFile(String lines, String fileName) throws FileWriterException {
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(fileName))) {
            writer.write(lines);
            writer.flush();
        } catch (IOException e) {
            System.out.println("SOS WASN'T SAVED, reason: " + e.getMessage());
            throw new FileWriterException(e);
        }
    }
}
