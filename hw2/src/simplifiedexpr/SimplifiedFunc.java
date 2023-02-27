package simplifiedexpr;

import java.math.BigInteger;
import java.util.Objects;

public class SimplifiedFunc implements SimplifiedBase {
    private final String funcName;
    private final SimplifiedExpr simplifiedExpr;

    public SimplifiedFunc(String funcName, SimplifiedExpr simplifiedExpr) {
        this.funcName = funcName;
        this.simplifiedExpr = simplifiedExpr;
    }

    public SimplifiedFunc(SimplifiedFunc simplifiedFunc) {
        funcName = simplifiedFunc.funcName;
        simplifiedExpr = new SimplifiedExpr(simplifiedFunc.simplifiedExpr);
    }

    public String getFuncName() {
        return funcName;
    }

    public SimplifiedExpr getSimplifiedExpr() {
        return simplifiedExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplifiedFunc that = (SimplifiedFunc) o;
        return Objects.equals(funcName, that.funcName) &&
                Objects.equals(simplifiedExpr, that.simplifiedExpr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, simplifiedExpr);
    }

    public String toString() {
        if (simplifiedExpr.getSimplifiedTerms().size() == 1) {
            SimplifiedTerm simplifiedTerm = simplifiedExpr.
                    getSimplifiedTerms().keySet().iterator().next();
            if ((simplifiedTerm.getSimplifiedBases().size() == 1 &&
                    simplifiedExpr.get(simplifiedTerm).equals(BigInteger.ONE)) ||
                    simplifiedTerm.getSimplifiedBases().size() == 0) {
                return funcName + "(" + simplifiedExpr + ")";
            } else {
                return funcName + "((" + simplifiedExpr + "))";
            }
        } else {
            return funcName + "((" + simplifiedExpr + "))";
        }
    }
}
