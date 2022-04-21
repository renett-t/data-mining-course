package ru.renett.hash;

import ru.renett.Doubleton;

import java.util.*;

public abstract class HashTable {
    private Map<Integer, Bucket> buckets = new HashMap<>();
    private final int k;

    public HashTable(int k) {
        this.k = k;
    }

    public abstract int calculate_hash(int i, int j);

    public int addDoubleton(Doubleton doubleton) {
        int hash = calculate_hash(doubleton.getI(), doubleton.getJ());
        if (buckets.containsKey(hash)) {
            Bucket bucket = buckets.get(hash);
            bucket.incrementDoubletonCount(doubleton);
            buckets.put(hash, bucket);
        } else {
            Bucket newB = new Bucket();
            newB.addNewDoubleton(doubleton);
            buckets.put(hash, newB);
        }
        return hash;
    }

    public int getK() {
        return k;
    }

    public int getBucketsCount() {
        return this.k;
    }

    public int getBucketSize(int i) {
        Bucket bucket = buckets.get(i);
        if (bucket == null) {
            return 0;
        }
        int res = 0;
        for (Pair p : bucket.getPairs()) {
            res += p.getCount();
        }
        return res;
    }

    public Set<? extends Doubleton> getBucketDoubletons(int i) {
        Bucket bucket = buckets.get(i);
        Set<Doubleton> doubletons = new HashSet<>();
        if (bucket == null) {
            return doubletons;
        }
        for (Pair p : bucket.getPairs()) {
            doubletons.add(p.getDoubleton());
        }

        return doubletons;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("HashTable:\n");
        for (int i = 0; i < k; i++) {
            sb.append("[").append(i).append("]").append(" = ");
            if (buckets.containsKey(i)) {
                Bucket bucket = buckets.get(i);
                for (Pair pair : bucket.getPairs()) {
                    sb.append(pair.getDoubleton().toString()).append("~").append(pair.getCount());
                    sb.append("  ");
                }
            } else {
                sb.append("no items");
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
