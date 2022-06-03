package ru.renett.hash;

import ru.renett.Doubleton;

import java.util.ArrayList;
import java.util.List;

public class Bucket {
    private List<Pair> pairs;

    public Bucket() {
        this.pairs = new ArrayList<>();
    }

    public List<Pair> getPairs() {
        return pairs;
    }

    public int getPairsSize() {
        return pairs.size();
    }

    public void addNewDoubleton(Doubleton doubleton) {
        pairs.add(new Pair(doubleton));
    }

    public void incrementDoubletonCount(Doubleton doubleton) {
        if (pairs.contains( new Pair(doubleton)) ) {
            for (Pair p : pairs) {
                if (p.getDoubleton().equals(doubleton)) {
                    p.incrementCount();
                }
            }
        } else {
            addNewDoubleton(doubleton);
        }
    }
}
