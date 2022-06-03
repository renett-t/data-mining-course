package ru.renett.data;

import ru.renett.Transaction;

import java.util.List;

public class Box {
    List<Integer> items;
    List<Transaction> transactions;

    public Box(List<Integer> items, List<Transaction> transactions) {
        this.transactions = transactions;
        this.items = items;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public List<Integer> getItems() {
        return items;
    }


    public String transactionsToString() {
        return transactions.toString();
    }
}
