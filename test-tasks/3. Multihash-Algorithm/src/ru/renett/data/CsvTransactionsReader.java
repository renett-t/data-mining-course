package ru.renett.data;

import ru.renett.Transaction;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.renett.Main.FILE_NAME;

public class CsvTransactionsReader {
    public static String PRODUCTS_FILE_NAME = "/home/renett/Repositories/python/data-mining-course/3. Multihash-Algorithm/src/ru/renett/data/products-mapping.csv";
    public static String DELIMITER = ";";

    public static Box readTransactionsFromFile(String file) throws IOException {
        List<List<String>> records = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file)))  {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(DELIMITER);
                records.add(Arrays.asList(values));
            }
        } catch (IOException e) {
            throw e;
        }

        int itemsCount = 1;
        Map<String, Integer> itemsMapping = new HashMap<>();
        Map<String, List<String>> transactions = new HashMap<>();

        for (int i = 1; i < records.size(); i++) {
            List<String> row = records.get(i);
            String product = row.get(0);
            String transaction = row.get(1);

            if (!itemsMapping.containsKey(product)) {
                itemsMapping.put(product, itemsCount++);
            }

            if (transactions.containsKey(transaction)) {
                transactions.get(transaction).add(product);
            } else {
                transactions.put(transaction, new ArrayList<>(List.of(product)));
            }
        }

        List<Transaction> transactionsResult = new ArrayList<>();

        for (Map.Entry<String, List<String>> entry : transactions.entrySet()) {
            List<Integer> items = new ArrayList<>();
            for (String str : entry.getValue()) {
                items.add(itemsMapping.get(str));
            }
            transactionsResult.add(new Transaction(items));
        }

        writeItemsMappings(itemsMapping);

        Box box = new Box(new ArrayList<>(itemsMapping.values().stream().sorted().collect(Collectors.toList())),
                transactionsResult);
        return box;
    }

    private static void writeItemsMappings(Map<String, Integer> itemsMapping) throws IOException {
        List<String[]> dataLines = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : itemsMapping.entrySet()) {
            dataLines.add(new String[]{entry.getKey(), String.valueOf(entry.getValue())});
        }

        File csvOutput = new File(PRODUCTS_FILE_NAME);
        try (PrintWriter pw = new PrintWriter(csvOutput)) {
            dataLines.stream()
                    .map(strings -> Stream.of(strings)
                            .collect(Collectors.joining(DELIMITER)))
                    .forEach(pw::println);
        }
    }
}
