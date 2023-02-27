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
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        for (Term term : terms) {
            simplifiedExpr = simplifiedExpr.add(term.simplify());
        }
        return simplifiedExpr;
    }
}
