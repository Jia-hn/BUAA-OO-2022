package expr;

import simplifiedexpr.SimplifiedExpr;

public class Factor {
    private final Base base;
    private final int exponent;

    public Factor(Base base, int exponent) {
        this.base = base;
        this.exponent = exponent;
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = base.simplify();
        simplifiedExpr = simplifiedExpr.pow(exponent);
        return simplifiedExpr;
    }
}
