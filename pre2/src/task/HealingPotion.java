package task;

import java.math.BigInteger;

public class HealingPotion extends Bottle {
    private double efficiency;

    public HealingPotion(int id, String name, BigInteger price,
                         double capacity, double efficiency) {
        super(id, name, price, capacity);
        this.efficiency = efficiency;
    }

    public void use(Adventurer adventurer) {
        if (getFilled()) {
            super.use(adventurer);
            adventurer.setHealth(adventurer.getHealth() + getCapacity() * efficiency);
            System.out.println(adventurer.getName() +
                    " recovered extra " + getCapacity() * efficiency + ".");
        } else {
            System.out.println("Failed to use " + getName() + " because it is empty.");
        }
    }

    public String toString() {
        return "The healingPotion's id is " + getId() + ", name is " + getName() +
                ", capacity is " + getCapacity() + ", filled is " + getFilled() +
                ", efficiency is " + efficiency + ".";
    }
}
