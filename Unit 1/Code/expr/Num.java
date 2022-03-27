package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Num implements Base {
    private final BigInteger constant;

    public Num(BigInteger constant) {
        this.constant = constant;
    }

    public Num deepCopy() {
        return new Num(constant);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Num num1 = (Num) o;
        return Objects.equals(constant, num1.constant);
    }

    @Override
    public int hashCode() {
        return Objects.hash(constant);
    }

    public Expr simplify() {
        Expr expr = new Expr();
        expr.put(new Term(), constant);
        return expr;
    }

    public Expr optimize1() {
        return null;
    }

    public Num optimize2() {
        return null;
    }

    public Num optimize3() {
        return null;
    }

    public String toString() {
        return String.valueOf(constant);
    }
}
