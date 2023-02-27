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
        SimplifiedExpr simplifiedExpr = factors.get(0).simplify();
        for (int i = 1; i < factors.size(); i++) {
            simplifiedExpr = simplifiedExpr.multiply(factors.get(i).simplify());
        }
        if (sign == -1) {
            simplifiedExpr = simplifiedExpr.negate();
        }
        return simplifiedExpr;
    }
}