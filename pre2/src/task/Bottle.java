package task;

import java.math.BigInteger;

public class Bottle extends Equipment {
    private double capacity;
    private boolean filled;

    public Bottle(int id, String name, BigInteger price, double capacity) {
        super(id, name, price);
        this.capacity = capacity;
        filled = true;
    }

    public double getCapacity() {
        return capacity;
    }

    public boolean getFilled() {
        return filled;
    }

    public void use(Adventurer adventurer) {
        if (filled) {
            adventurer.setHealth(adventurer.getHealth() + capacity / 10);
            filled = false;
            setPrice(getPrice().divide(BigInteger.TEN));
            System.out.println(adventurer.getName() + " drank " + getName() +
                    " and recovered " + capacity / 10 + ".");
        } else {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        }
    }

    public String toString() {
        return "The bottle's id is " + getId() + ", name is " + getName() +
                ", capacity is " + capacity + ", filled is " + filled + ".";
    }
}