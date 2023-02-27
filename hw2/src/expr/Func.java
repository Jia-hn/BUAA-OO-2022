package expr;

import simplifiedexpr.SimplifiedExpr;
import simplifiedexpr.SimplifiedFunc;
import simplifiedexpr.SimplifiedTerm;

import java.math.BigInteger;

public class Func implements Base {
    private final String funcName;
    private final Expr expr;

    public Func(String funcName, Expr expr) {
        this.funcName = funcName;
        this.expr = expr;
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr1 = new SimplifiedExpr();
        SimplifiedTerm simplifiedTerm = new SimplifiedTerm();
        SimplifiedExpr simplifiedExpr2 = expr.simplify();
        if (simplifiedExpr2.getSimplifiedTerms().size() == 0) {
            if (funcName.equals("sin")) {
                simplifiedExpr1.put(simplifiedTerm, BigInteger.ZERO);
            } else {
                simplifiedExpr1.put(simplifiedTerm, BigInteger.ONE);
            }
        } else if (simplifiedExpr2.get(simplifiedExpr2.getSimplifiedTerms().keySet().
                        iterator().next()).compareTo(BigInteger.ZERO) < 0) {
            simplifiedExpr2 = simplifiedExpr2.multiply(SimplifiedExpr.one().negate());
            if (funcName.equals("sin")) {
                simplifiedTerm.put(new SimplifiedFunc(funcName, simplifiedExpr2), 1);
                simplifiedExpr1.put(simplifiedTerm, BigInteger.ONE.negate());
            } else {
                simplifiedTerm.put(new SimplifiedFunc(funcName, simplifiedExpr2), 1);
                simplifiedExpr1.put(simplifiedTerm, BigInteger.ONE);
            }
        } else {
            simplifiedTerm.put(new SimplifiedFunc(funcName, simplifiedExpr2), 1);
            simplifiedExpr1.put(simplifiedTerm, BigInteger.ONE);
        }
        return simplifiedExpr1;
    }
}
