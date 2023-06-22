package main;

public class Relation implements Comparable<Relation> {
    private final int id1;
    private final int id2;
    private final int value;

    public Relation(int id1, int id2, int value) {
        this.id1 = id1;
        this.id2 = id2;
        this.value = value;
    }

    public int getId1() {
        return id1;
    }

    public int getId2() {
        return id2;
    }

    public int getValue() {
        return value;
    }

    public int compareTo(Relation obj) {
        return value - obj.value;
    }
}
