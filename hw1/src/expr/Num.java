package expr;

import simplifiedexpr.SimplifiedExpr;
import java.math.BigInteger;

public class Num implements Base {
    private final BigInteger num;

    public Num(BigInteger num) {
        this.num = num;
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        simplifiedExpr.getCoefficients()[0] = new BigInteger(String.valueOf(num));
        return simplifiedExpr;
    }
}