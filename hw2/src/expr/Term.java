package expr;

import simplifiedexpr.SimplifiedExpr;

import java.util.ArrayList;

public class Term {
    private int sign;
    private final ArrayList<Factor> factors;

    public Term() {
        sign = 1;
        this.factors = new ArrayList<>();
    }

    public void changeSign() {
        this.sign *= -1;
    }

    public void addFactor(Factor factor) {
        factors.add(factor);
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = SimplifiedExpr.one();
        for (Factor factor : factors) {
            simplifiedExpr = simplifiedExpr.multiply(factor.simplify());
        }
        if (sign == -1) {
            simplifiedExpr = simplifiedExpr.negate();
        }
        return simplifiedExpr;
    }
}
