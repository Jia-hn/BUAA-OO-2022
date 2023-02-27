package expr;

import simplifiedexpr.SimplifiedExpr;
import java.math.BigInteger;

public class Var implements Base {
    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        simplifiedExpr.getCoefficients()[1] = BigInteger.ONE;
        return simplifiedExpr;
    }
}