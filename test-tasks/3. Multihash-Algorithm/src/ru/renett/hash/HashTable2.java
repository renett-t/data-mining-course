package ru.renett.hash;

public class HashTable2 extends HashTable {

    public HashTable2(int k) {
        super(k);
    }

    @Override
    public int calculate_hash(int i, int j) {
        return (2 * i + 3 * j + i*j*j) % super.getK();
    }
}
