package ru.renett.data;

import ru.renett.Doubleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Set;

public class TxtResultWriter {
    public static String RESULT_FILE_NAME = "/home/renett/Repositories/java/Multihash-Algorithm/Multihash-Algorithm/src/ru/renett/data/result.txt";

    public static void writeResultToFile(Set<Integer> resultSingletons, Set<Doubleton> resultDoubletons) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(RESULT_FILE_NAME))) {
            writer.write("SINGLETONS:\n");
            writer.write(resultSingletons.toString());

            writer.write("\nDOUBLETONES:\n");
            writer.write(resultDoubletons.toString());
        }
    }
}
