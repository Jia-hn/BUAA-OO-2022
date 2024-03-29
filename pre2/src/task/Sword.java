package task;

import java.math.BigInteger;

public class Sword extends Equipment {
    private double sharpness;

    public Sword(int id, String name, BigInteger price, double sharpness) {
        super(id, name, price);
        this.sharpness = sharpness;
    }

    public double getSharpness() {
        return sharpness;
    }

    public void setSharpness(double sharpness) {
        this.sharpness = sharpness;
    }

    public void use(Adventurer adventurer) {
        adventurer.setHealth(adventurer.getHealth() - 10.0);
        adventurer.setExp(adventurer.getExp() + 10.0);
        adventurer.setMoney(adventurer.getMoney() + sharpness);
        System.out.println(adventurer.getName() + " used " + getName() +
                " and earned " + sharpness + ".");
    }

    public String toString() {
        return "The sword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + sharpness + ".";
    }
}
