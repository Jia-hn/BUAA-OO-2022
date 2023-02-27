package task;

import java.math.BigInteger;

public abstract class SubstanceOfValue implements Comparable<SubstanceOfValue> {
    private int id;
    private String name;

    public SubstanceOfValue(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract BigInteger getPrice();

    public int compareTo(SubstanceOfValue substanceOfValue) {
        if (this.getPrice().compareTo(substanceOfValue.getPrice()) > 0) {
            return 1;
        } else if (this.getPrice().compareTo(substanceOfValue.getPrice()) < 0) {
            return -1;
        } else {
            return this.id - substanceOfValue.id;
        }
    }

    public abstract void use(Adventurer adventurer);
}
