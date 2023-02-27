package expr;

import simplifiedexpr.SimplifiedExpr;
import simplifiedexpr.SimplifiedTerm;
import simplifiedexpr.SimplifiedVar;

import java.math.BigInteger;

public class Var implements Base {
    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        SimplifiedTerm simplifiedTerm = new SimplifiedTerm();
        simplifiedTerm.put(new SimplifiedVar(),1);
        simplifiedExpr.put(simplifiedTerm, BigInteger.ONE);
        return simplifiedExpr;
    }
}
