package task;

import java.math.BigInteger;

public abstract class Equipment extends SubstanceOfValue {
    private BigInteger price;

    public Equipment(int id, String name, BigInteger price) {
        super(id,name);
        this.price = price;
    }

    public BigInteger getPrice() {
        return price;
    }

    public void setPrice(BigInteger price) {
        this.price = price;
    }

    public abstract void use(Adventurer adventurer);

}
