package expr;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class Expr implements Base {
    private final HashMap<Term, BigInteger> terms;

    public Expr() {
        terms = new HashMap<>();
    }

    public Expr deepCopy() {
        Expr expr = new Expr();
        for (Term term : this.terms.keySet()) {
            expr.put(term.deepCopy(), this.get(term));
        }
        return expr;
    }

    public static Expr one() {
        Expr expr = new Expr();
        expr.put(new Term(), BigInteger.ONE);
        return expr;
    }

    public static Expr two() {
        Expr expr = new Expr();
        expr.put(new Term(), new BigInteger("2"));
        return expr;
    }

    public HashMap<Term, BigInteger> getTerms() {
        return terms;
    }

    public BigInteger get(Term term) {
        return terms.get(term);
    }

    public void put(Term term, BigInteger coefficient) {
        if (terms.containsKey(term)) {
            terms.put(term, terms.get(term).add(coefficient));
        } else {
            terms.put(term, coefficient);
        }
        if (terms.get(term).equals(BigInteger.ZERO)) {
            terms.remove(term);
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
        Expr that = (Expr) o;
        return Objects.equals(terms, that.terms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(terms);
    }

    public Expr add(Expr augend) {
        Expr result = this.deepCopy();
        for (Term term : augend.terms.keySet()) {
            result.put(term.deepCopy(), augend.get(term));
        }
        return result;
    }

    public Expr multiply(Expr multiplicand) {
        Expr result = new Expr();
        for (Term term1 : this.terms.keySet()) {
            for (Term term2 : multiplicand.terms.keySet()) {
                BigInteger bigInteger = this.get(term1).multiply(multiplicand.get(term2));
                Term term = term1.multiply(term2);
                Expr expr = new Expr();
                expr.put(term, bigInteger);
                result = result.add(expr);
            }
        }
        return result;
    }

    public Expr negate() {
        Expr result = new Expr();
        for (Term term : terms.keySet()) {
            result.put(term, get(term).negate());
        }
        return result;
    }

    public Expr pow(int n) {
        Expr result = Expr.one();
        for (int i = 0; i < n; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public Expr simplify() {
        Expr expr = new Expr();
        for (Term term : terms.keySet()) {
            Expr temp = new Expr();
            temp.put(new Term(), terms.get(term));
            expr = expr.add(term.simplify().multiply(temp));
        }
        return expr;
    }

    public Expr optimize1() {
        Expr expr = new Expr();
        for (Term term : terms.keySet()) {
            Expr temp = new Expr();
            temp.put(new Term(), terms.get(term));
            expr = expr.add(term.optimize1().multiply(temp));
        }
        return expr;
    }

    private void func(Term term1, Term term2, Term temp) {
        put(temp, get(term1));
        put(term2, get(term1).negate());
        terms.remove(term1);
    }

    private void func2(BigInteger coefficient1, BigInteger coefficient2,
                       Term term1, Term term2, Term temp) {
        if (coefficient1.compareTo(BigInteger.ZERO) > 0 &&
                coefficient2.compareTo(BigInteger.ZERO) > 0) {
            if (coefficient1.compareTo(coefficient2) <= 0) {
                func(term1, term2, temp);
            } else {
                func(term2, term1, temp);
            }
        } else if (coefficient1.compareTo(BigInteger.ZERO) < 0 &&
                coefficient2.compareTo(BigInteger.ZERO) < 0) {
            if (coefficient1.compareTo(coefficient2) <= 0) {
                func(term2, term1, temp);
            } else {
                func(term1, term2, temp);
            }
        } else if (coefficient1.compareTo(BigInteger.ZERO) > 0 &&
                coefficient2.compareTo(BigInteger.ZERO) < 0) {
            func(term2, term1, temp);
        } else if (coefficient1.compareTo(BigInteger.ZERO) < 0 &&
                coefficient2.compareTo(BigInteger.ZERO) > 0) {
            func(term1, term2, temp);
        }
    }

    private void merge1() {
        boolean flag = true;
        while (flag) {
            flag = false;
            OUT:
            for (Term term1 : terms.keySet()) {
                Term temp1 = term1.deepCopy();
                ArrayList<Base> bases = temp1.func();
                for (Base base1 : bases) {
                    Base base2 = new Func("cos", ((Func) base1).getExpr().deepCopy());
                    temp1.put(base1, -2);
                    for (Term term2 : terms.keySet()) {
                        Term temp2 = term2.deepCopy();
                        if (term1 == term2) {
                            continue;
                        }
                        if (temp2.getBases().containsKey(base2) && temp2.get(base2) >= 2) {
                            temp2.put(base2, -2);
                            if (temp1.equals(temp2)) {
                                func2(get(term1), get(term2), term1, term2, temp1);
                                flag = true;
                                break OUT;
                            }
                        }
                    }
                }
            }
        }
    }

    public Expr optimize2() {
        Expr expr = new Expr();
        for (Term term : terms.keySet()) {
            expr.put(term.optimize2(), get(term));
        }
        expr.merge1();
        return expr;
    }

    private void merge2() {
        boolean flag = true;
        while (flag) {
            flag = false;
            OUT:
            for (Term term : terms.keySet()) {
                for (Base base1 : term.getBases().keySet()) {
                    if (!(base1 instanceof Func) || ((Func) base1).getFuncName().equals("cos")) {
                        continue;
                    }
                    Base base2 = new Func("cos", ((Func) base1).getExpr().deepCopy());
                    Integer exponent1 = term.get(base1);
                    Integer exponent2 = term.get(base2);
                    if (term.getBases().containsKey(base2) && exponent1.equals(exponent2) && get(
                            term).mod(new BigInteger("2").pow(exponent1)).equals(BigInteger.ZERO)) {
                        Expr expr = ((Func) base1).getExpr().deepCopy();
                        expr = expr.multiply(Expr.two());
                        Base base3 = new Func("sin", expr);
                        Term temp = term.deepCopy();
                        temp.getBases().remove(base1);
                        temp.getBases().remove(base2);
                        temp.put(base3, exponent1);
                        put(temp, get(term).divide(new BigInteger("2").pow(exponent1)));
                        terms.remove(term);
                        flag = true;
                        break OUT;
                    }
                }
            }
        }
    }

    public Expr optimize3() {
        Expr expr = new Expr();
        for (Term term : terms.keySet()) {
            expr.put(term.optimize3(), get(term));
        }
        expr.merge2();
        return expr;
    }

    public Expr optimize() {
        Expr expr1 = this.deepCopy();
        Expr expr2 = this.deepCopy();
        boolean flag = true;
        while (flag || !String.valueOf(expr1).equals(String.valueOf(expr2))) {
            expr1 = expr2.deepCopy();
            expr2 = expr1.optimize1().optimize2().optimize3();
            flag = false;
        }
        return expr1;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        Term temp = null;
        for (Term term : terms.keySet()) {
            if (get(term).compareTo(BigInteger.ZERO) > 0) {
                stringBuilder.append("+");
                stringBuilder.append(get(term));
                if (term.getBases().size() != 0) {
                    stringBuilder.append("*");
                    stringBuilder.append(term);
                }
                temp = term;
                break;
            }
        }
        for (Term term : terms.keySet()) {
            if (term != temp) {
                stringBuilder.append("+");
                stringBuilder.append(get(term));
                if (term.getBases().size() != 0) {
                    stringBuilder.append("*");
                    stringBuilder.append(term);
                }
            }
        }
        String ret = String.valueOf(stringBuilder);
        ret = ret.replaceAll("sin\\(x\\*x\\)", "sin(x**2)");
        ret = ret.replaceAll("cos\\(x\\*x\\)", "cos(x**2)");
        ret = ret.replaceAll("\\+1\\*", "+");
        ret = ret.replaceAll("-1\\*", "-");
        ret = ret.replaceAll("\\+-", "-");
        if (ret.equals("")) {
            ret = "0";
        }
        if (ret.charAt(0) == '+') {
            ret = ret.substring(1);
        }
        return ret;
    }
}
