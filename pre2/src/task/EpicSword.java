package task;

import java.math.BigInteger;

public class EpicSword extends Sword {
    private double evolveRatio;

    public EpicSword(int id, String name, BigInteger price, double sharpness, double evolveRatio) {
        super(id, name, price, sharpness);
        this.evolveRatio = evolveRatio;
    }

    public void use(Adventurer adventurer) {
        super.use(adventurer);
        setSharpness(getSharpness() * evolveRatio);
        System.out.println(getName() + "'s sharpness became " + getSharpness() + ".");
    }

    public String toString() {
        return "The epicSword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ", evolveRatio is " + evolveRatio + ".";
    }
}
