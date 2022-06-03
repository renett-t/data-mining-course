package ru.renett.hash;

import ru.renett.Doubleton;

import java.util.Objects;

public class Pair {
    private Doubleton doubleton;
    private int count;

    public Pair(Doubleton doubleton, int count) {
        this.doubleton = doubleton;
        this.count = count;
    }

    public Pair(Doubleton doubleton) {
        this(doubleton,1);
    }

    public void incrementCount() {
        this.count = getCount() + 1;
    }

    public Doubleton getDoubleton() {
        return doubleton;
    }

    public void setDoubleton(Doubleton doubleton) {
        this.doubleton = doubleton;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return Objects.equals(doubleton, pair.doubleton);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doubleton);
    }
}
