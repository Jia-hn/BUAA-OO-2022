package expr;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Term {
    private final HashMap<Base, Integer> bases;

    public Term() {
        bases = new HashMap<>();
    }

    public Term deepCopy() {
        Term term = new Term();
        for (Base base : this.bases.keySet()) {
            term.put(base.deepCopy(), this.get(base));
        }
        return term;
    }

    public HashMap<Base, Integer> getBases() {
        return bases;
    }

    public Integer get(Base base) {
        return bases.get(base);
    }

    public void put(Base base, Integer exponent) {
        if (bases.containsKey(base)) {
            bases.put(base, bases.get(base) + exponent);
        } else {
            bases.put(base, exponent);
        }
        if (bases.get(base).equals(0)) {
            bases.remove(base);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Term that = (Term) o;
        return Objects.equals(bases, that.bases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bases);
    }

    public Term multiply(Term multiplicand) {
        Term result = this.deepCopy();
        for (Base base : multiplicand.bases.keySet()) {
            result.put(base.deepCopy(), multiplicand.get(base));
        }
        return result;
    }

    public Expr simplify() {
        Expr expr = Expr.one();
        for (Base base : bases.keySet()) {
            expr = expr.multiply(base.simplify().pow(get(base)));
        }
        return expr;
    }

    public Expr optimize1() {
        Expr expr = Expr.one();
        for (Base base : bases.keySet()) {
            expr = expr.multiply(base.optimize1().pow(get(base)));
        }
        return expr;
    }

    public ArrayList<Base> func() {
        ArrayList<Base> ret = new ArrayList<>();
        for (Base base : bases.keySet()) {
            if (base instanceof Func && ((Func) base).
                    getFuncName().equals("sin") && get(base) >= 2) {
                ret.add(base.deepCopy());
            }
        }
        return ret;
    }

    public Term optimize2() {
        Term term = new Term();
        for (Base base : bases.keySet()) {
            term.put(base.optimize2(), get(base));
        }
        return term;
    }

    public Term optimize3() {
        Term term = new Term();
        for (Base base : bases.keySet()) {
            term.put(base.optimize3(), get(base));
        }
        return term;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Base base : bases.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("*");
            }
            stringBuilder.append(base);
            if (base instanceof Var && get(base).equals(2)) {
                stringBuilder.append("*x");
            } else if (!get(base).equals(1)) {
                stringBuilder.append("**");
                stringBuilder.append(get(base));
            }
        }
        return String.valueOf(stringBuilder);
    }
}
