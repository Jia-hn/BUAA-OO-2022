package expr;

import simplifiedexpr.SimplifiedExpr;
import simplifiedexpr.SimplifiedTerm;
import java.math.BigInteger;

public class Num implements Base {
    private final BigInteger num;

    public Num(BigInteger num) {
        this.num = num;
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        simplifiedExpr.put(new SimplifiedTerm(), num);
        return simplifiedExpr;
    }
}
