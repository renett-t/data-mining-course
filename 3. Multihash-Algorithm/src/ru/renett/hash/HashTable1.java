package ru.renett.hash;

public class HashTable1 extends HashTable {

    public HashTable1(int k) {
        super(k);
    }

    @Override
    public int calculate_hash(int i, int j) {
        return (i + j) % super.getK();
    }
}
