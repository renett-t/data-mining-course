package ru.renett;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final List<Integer> items;
    private List<Doubleton> doubletons;

    public Transaction(List<Integer> items) {
        this.items = items;
    }

    public List<Doubleton> generateDoubletones() {
        List<Doubleton> doubletonsList = new ArrayList<>();
        for (int i = 0; i < items.size() - 1; i++) {
            for (int j = i + 1; j < items.size(); j++) {
                Doubleton newDoubleton = new Doubleton(items.get(i), items.get(j));
                doubletonsList.add(newDoubleton);
            }
        }

        this.doubletons = doubletonsList;
        return doubletonsList;
    }

    public List<Integer> getItems() {
        return items;
    }

    public List<Doubleton> getDoubletons() {
        return doubletons;
    }

    @Override
    public String toString() {
        return "Transaction {" + items + '}';
    }
}
