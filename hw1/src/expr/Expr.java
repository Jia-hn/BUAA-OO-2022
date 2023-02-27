package expr;

import simplifiedexpr.SimplifiedExpr;
import java.util.ArrayList;

public class Expr implements Base {
    private final ArrayList<Term> terms;

    public Expr() {
        this.terms = new ArrayList<>();
    }

    public ArrayList<Term> getTerms() {
        return terms;
    }

    public void addTerm(Term term) {
        terms.add(term);
    }

    public SimplifiedExpr simplify() {
        SimplifiedExpr simplifiedExpr = terms.get(0).simplify();
        for (int i = 1; i < terms.size(); i++) {
            simplifiedExpr = simplifiedExpr.add(terms.get(i).simplify());
        }
        return simplifiedExpr;
    }
}