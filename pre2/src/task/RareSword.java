package task;

import java.math.BigInteger;

public class RareSword extends Sword {
    private double extraExpBonus;

    public RareSword(int id, String name, BigInteger price,
                     double sharpness, double extraExpBonus) {
        super(id, name, price, sharpness);
        this.extraExpBonus = extraExpBonus;
    }

    public void use(Adventurer adventurer) {
        super.use(adventurer);
        adventurer.setExp(adventurer.getExp() + extraExpBonus);
        System.out.println(adventurer.getName() + " got extra exp " + extraExpBonus + ".");
    }

    public String toString() {
        return "The rareSword's id is " + getId() + ", name is " + getName() +
                ", sharpness is " + getSharpness() + ", extraExpBonus is " + extraExpBonus + ".";
    }
}
