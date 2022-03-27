package expr;

import java.math.BigInteger;
import java.util.Objects;

public class Func implements Base {
    private final String funcName;
    private final Expr expr;

    public Func(String funcName, Expr expr) {
        this.funcName = funcName;
        this.expr = expr;
    }

    public Func deepCopy() {
        return new Func(funcName, expr.deepCopy());
    }

    public String getFuncName() {
        return funcName;
    }

    public Expr getExpr() {
        return expr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Func that = (Func) o;
        return Objects.equals(funcName, that.funcName) &&
                Objects.equals(expr, that.expr);
    }

    @Override
    public int hashCode() {
        return Objects.hash(funcName, expr);
    }

    public Expr simplify() {
        Expr expr = new Expr();
        Term term = new Term();
        term.put(new Func(funcName, this.expr.simplify()), 1);
        expr.put(term, BigInteger.ONE);
        return expr;
    }

    public Expr optimize1() {
        Expr expr1 = new Expr();
        Term term = new Term();
        Expr expr2 = expr.optimize1();
        if (expr2.getTerms().size() == 0) {
            if (funcName.equals("sin")) {
                expr1.put(term, BigInteger.ZERO);
            } else {
                expr1.put(term, BigInteger.ONE);
            }
        } else if (expr2.get(expr2.getTerms().keySet().iterator().next()).
                compareTo(BigInteger.ZERO) < 0) {
            expr2 = expr2.multiply(Expr.one().negate());
            if (funcName.equals("sin")) {
                term.put(new Func(funcName, expr2), 1);
                expr1.put(term, BigInteger.ONE.negate());
            } else {
                term.put(new Func(funcName, expr2), 1);
                expr1.put(term, BigInteger.ONE);
            }
        } else {
            term.put(new Func(funcName, expr2), 1);
            expr1.put(term, BigInteger.ONE);
        }
        return expr1;
    }

    public Func optimize2() {
        return new Func(funcName, expr.optimize2());
    }

    public Func optimize3() {
        return new Func(funcName, expr.optimize3());
    }

    public String toString() {
        if (expr.getTerms().size() == 1) {
            Term term = expr.getTerms().keySet().iterator().next();
            if ((term.getBases().size() == 1 && expr.get(term).equals(BigInteger.ONE))
                    || term.getBases().size() == 0) {
                return funcName + "(" + expr + ")";
            } else {
                return funcName + "((" + expr + "))";
            }
        } else {
            return funcName + "((" + expr + "))";
        }
    }
}
