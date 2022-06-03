package ru.renett;

import java.util.Objects;

public class Doubleton {
    private int i;
    private int j;

    public Doubleton(int i, int j) {
        this.i = i;
        this.j = j;
    }

    public int getI() {
        return i;
    }

    public int getJ() {
        return j;
    }

    @Override
    public String toString() {
        return "{" + i + ", " + j + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Doubleton)) return false;
        Doubleton doubleton = (Doubleton) o;
        return i == doubleton.i && j == doubleton.j;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j);
    }
}
