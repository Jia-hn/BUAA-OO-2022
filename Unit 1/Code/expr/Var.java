package expr;

import java.math.BigInteger;

public class Var implements Base {
    public Var() {

    }

    public Var deepCopy() {
        return new Var();
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Var;
    }

    @Override
    public int hashCode() {
        return 1;
    }

    public Expr simplify() {
        Expr expr = new Expr();
        Term term = new Term();
        term.put(new Var(), 1);
        expr.put(term, BigInteger.ONE);
        return expr;
    }

    public Expr optimize1() {
        Expr expr = new Expr();
        Term term = new Term();
        term.put(new Var(), 1);
        expr.put(term, BigInteger.ONE);
        return expr;
    }

    public Var optimize2() {
        return new Var();
    }

    public Var optimize3() {
        return new Var();
    }

    public String toString() {
        return "x";
    }
}
