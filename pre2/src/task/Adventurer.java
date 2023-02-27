package task;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class Adventurer extends SubstanceOfValue {
    private double health;
    private double exp;
    private double money;
    private HashMap<Integer, SubstanceOfValue> substanceOfValues;

    public Adventurer(int id, String name) {
        super(id,name);
        this.health = 100.0;
        this.exp = 0.0;
        this.money = 0.0;
        this.substanceOfValues = new HashMap<>();
    }

    public double getHealth() {
        return health;
    }

    public double getExp() {
        return exp;
    }

    public double getMoney() {
        return money;
    }

    public HashMap<Integer, SubstanceOfValue> getSubstanceOfValues() {
        return substanceOfValues;
    }

    public BigInteger getPrice() {
        BigInteger price = BigInteger.ZERO;
        for (Integer integer : substanceOfValues.keySet()) {
            price = price.add(substanceOfValues.get(integer).getPrice());
        }
        return price;
    }

    public void setHealth(double health) {
        this.health = health;
    }

    public void setExp(double exp) {
        this.exp = exp;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void addEquipment1(int type, int id, String name, BigInteger price, double var1) {
        switch (type) {
            case 1:
                substanceOfValues.put(id, new Bottle(id, name, price, var1));
                break;
            case 4:
                substanceOfValues.put(id, new Sword(id, name, price, var1));
                break;
            default:
        }
    }

    public void addEquipment2(int type, int id, String name,
                              BigInteger price, double var1, double var2) {
        switch (type) {
            case 2:
                substanceOfValues.put(id, new HealingPotion(id, name, price, var1, var2));
                break;
            case 3:
                substanceOfValues.put(id, new ExpBottle(id, name, price, var1, var2));
                break;
            case 5:
                substanceOfValues.put(id, new RareSword(id, name, price, var1, var2));
                break;
            case 6:
                substanceOfValues.put(id, new EpicSword(id, name, price, var1, var2));
                break;
            default:
        }
    }

    public BigInteger sumOfPrice() {
        BigInteger sum = BigInteger.ZERO;
        for (Integer integer : substanceOfValues.keySet()) {
            sum = sum.add(substanceOfValues.get(integer).getPrice());
        }
        return sum;
    }

    public BigInteger maxOfPrice() {
        BigInteger max = BigInteger.ZERO;
        for (Integer integer : substanceOfValues.keySet()) {
            if (substanceOfValues.get(integer).getPrice().compareTo(max) > 0) {
                max = substanceOfValues.get(integer).getPrice();
            }
        }
        return max;
    }

    public int numOfSubstanceOfValue() {
        return substanceOfValues.size();
    }

    public void use(Adventurer adventurer) {
        ArrayList<SubstanceOfValue> arrayList = new ArrayList(substanceOfValues.values());
        Collections.sort(arrayList, Comparator.reverseOrder());
        for (SubstanceOfValue substanceOfValue : arrayList) {
            substanceOfValue.use(adventurer);
        }
    }

    public String toString() {
        return "The adventurer's id is " + getId() + ", name is " + getName() + ", health is " +
                health + ", exp is " + exp + ", money is " + money + ".";
    }
}