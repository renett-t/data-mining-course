package ru.renett;

import ru.renett.data.Box;
import ru.renett.data.CsvTransactionsReader;
import ru.renett.data.TxtResultWriter;
import ru.renett.hash.HashTable1;
import ru.renett.hash.HashTable2;
import ru.renett.hash.HashTable;

import java.io.IOException;
import java.util.*;

public class Main {
    public static String FILE_NAME = "/home/renett/Repositories/python/data-mining-course/3. Multihash-Algorithm/src/ru/renett/data/transactions.csv";

    public static void main(String[] args) throws IOException {
        int supportThreshold = 3;

//        Box boxWithData = readItemsAndTransactionsTest();
        Box boxWithData = readItemsAndTransactions(FILE_NAME);
        List<Integer> items = boxWithData.getItems();
        List<Transaction> transactions = boxWithData.getTransactions();

        HashTable1 hashTable1 = new HashTable1(items.size() + 1);
        HashTable2 hashTable2 = new HashTable2(items.size() + 1);

        for (Transaction transaction : transactions) {
            transaction.generateDoubletones();
            for (Doubleton d : transaction.getDoubletons()) {
                hashTable1.addDoubleton(d);
                hashTable2.addDoubleton(d);
            }
        }

        System.out.println("=======================");
        System.out.println(hashTable1);
        System.out.println("=======================");
        System.out.println(hashTable2);
        System.out.println("=======================");

        Set<Integer> blockSingletones = getSingltonesUnderSupportThreshold(supportThreshold, items, transactions);
        Set<Doubleton> blockDoubletons1 = getDoubletonsUnderSupportThreshold(supportThreshold, hashTable1);
        Set<Doubleton> blockDoubletons2 = getDoubletonsUnderSupportThreshold(supportThreshold, hashTable2);

        Set<Integer> resultSingletons = new HashSet<>();
        Set<Doubleton> resultDoubletons = new HashSet<>();
        for (int k : items) {
            if (!blockSingletones.contains(k)) {
                resultSingletons.add(k);
            }
        }
        Set<Doubleton> totalDoubleTones = new HashSet<>();
        for (Transaction transaction : transactions) {
            for (Doubleton d : transaction.getDoubletons()) {
                totalDoubleTones.add(d);
                if (!blockDoubletons1.contains(d) && !blockDoubletons2.contains(d)) {
                    if (!blockSingletones.contains(d.getI()) && !blockSingletones.contains(d.getJ())) {
                        resultDoubletons.add(d);
                    }
                }
            }
        }

        System.out.println("\n RESULT: \n");
//        writeResultToFile(resultSingletons, resultDoubletons);
        System.out.println(resultSingletons);
        System.out.println(resultDoubletons);
        System.out.println("Total products count = " + items.size());
        System.out.println("Total Doubletons count = " + totalDoubleTones.size());
        System.out.println("Frequent Singletons count = " + resultSingletons.size());
        System.out.println("Frequent Doubletons count = " + resultDoubletons.size());
    }

    private static void writeResultToFile(Set<Integer> resultSingletons, Set<Doubleton> resultDoubletons) throws IOException {
        TxtResultWriter.writeResultToFile(resultSingletons, resultDoubletons);
    }

    private static Set<Doubleton> getDoubletonsUnderSupportThreshold(int supportThreshold, HashTable hashTable) {
        Set<Doubleton> underThreshold = new HashSet<>();
        int bucketCount = hashTable.getBucketsCount();
        for (int i = 0; i < bucketCount; i++) {
            if (hashTable.getBucketSize(i) < supportThreshold) {
                underThreshold.addAll(hashTable.getBucketDoubletons(i));
            }
        }

        return underThreshold;
    }

    private static Set<Integer> getSingltonesUnderSupportThreshold(int supportThreshold, List<Integer> items, List<Transaction> transactions) {
        Set<Integer> underThreshold = new HashSet<>();
        Map<Integer, Integer> itemsCount = new HashMap<>();
        for (int i : items) {
            itemsCount.put(i, 0);
        }
        for (Transaction tr : transactions) {
            for (int item : tr.getItems()) {
                itemsCount.replace(item, itemsCount.get(item) + 1);
            }
        }

        for (Map.Entry<Integer, Integer> entry : itemsCount.entrySet()) {
            if (entry.getValue() < supportThreshold) {
                underThreshold.add(entry.getKey());
            }
        }

        return underThreshold;
    }

    private static Box readItemsAndTransactions(String file) throws IOException {
        return CsvTransactionsReader.readTransactionsFromFile(file);
    }

    private static Box readItemsAndTransactionsTest() {
        // Test data from presentation - https://datalaboratory.one/2021/07/08/lecture-6-an-introduction-into-datamining/
        // milk = 1
        // coke = 2
        // bread = 3
        // pepsi = 4
        // juice = 5
        Transaction t1 = new Transaction(List.of(1, 2, 3));
        Transaction t2 = new Transaction(List.of(1, 4, 5));
        Transaction t3 = new Transaction(List.of(1, 3));
        Transaction t4 = new Transaction(List.of(2, 5));
        Transaction t5 = new Transaction(List.of(1, 3, 4));
        Transaction t6 = new Transaction(List.of(1, 2, 3, 5));
        Transaction t7 = new Transaction(List.of(2, 3, 5));
        Transaction t8 = new Transaction(List.of(2, 3));

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(t1);
        transactions.add(t2);
        transactions.add(t3);
        transactions.add(t4);
        transactions.add(t5);
        transactions.add(t6);
        transactions.add(t7);
        transactions.add(t8);

        return new Box(List.of(1, 2, 3, 4, 5), transactions);
    }
}
