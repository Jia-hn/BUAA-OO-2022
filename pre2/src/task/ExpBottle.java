package task;

import java.math.BigInteger;

public class ExpBottle extends Bottle {
    private double expRatio;

    public ExpBottle(int id, String name, BigInteger price, double capacity, double expRatio) {
        super(id, name, price, capacity);
        this.expRatio = expRatio;
    }

    public void use(Adventurer adventurer) {
        if (getFilled()) {
            super.use(adventurer);
            adventurer.setExp(adventurer.getExp() * expRatio);
            System.out.println(adventurer.getName() + "'s exp became " + adventurer.getExp() + ".");
        } else {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        }
    }

    public String toString() {
        return "The expBottle's id is " + getId() + ", name is " + getName() + ", capacity is " +
                getCapacity() + ", filled is " + getFilled() + ", expRatio is " + expRatio + ".";
    }
}
