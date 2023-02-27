package parser;

import expr.Base;
import expr.Expr;
import expr.Factor;
import expr.Num;
import expr.Term;
import expr.Var;
import java.math.BigInteger;

public class Parser {
    private final Lexer lexer;

    public Parser(Lexer lexer) {
        this.lexer = lexer;
    }

    public Expr parseExpr() {
        Expr expr = new Expr();
        if (lexer.getCurToken().equals("+")) {
            lexer.next();
            expr.addTerm(parseTerm());
        } else if (lexer.getCurToken().equals("-")) {
            lexer.next();
            expr.addTerm(parseTerm());
            expr.getTerms().get(expr.getTerms().size() - 1).changeSign();
        } else {
            expr.addTerm(parseTerm());
        }
        while (lexer.getCurToken().equals("+") || lexer.getCurToken().equals("-")) {
            if (lexer.getCurToken().equals("+")) {
                lexer.next();
                expr.addTerm(parseTerm());
            } else if (lexer.getCurToken().equals("-")) {
                lexer.next();
                expr.addTerm(parseTerm());
                expr.getTerms().get(expr.getTerms().size() - 1).changeSign();
            }
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        if (lexer.getCurToken().equals("+")) {
            lexer.next();
            term.addFactor(parseFactor());
        } else if (lexer.getCurToken().equals("-")) {
            term.changeSign();
            lexer.next();
            term.addFactor(parseFactor());
        } else {
            term.addFactor(parseFactor());
        }
        while (lexer.getCurToken().equals("*")) {
            lexer.next();
            term.addFactor(parseFactor());
        }
        return term;
    }

    public Factor parseFactor() {
        return new Factor(parseBase(), parseExponent());
    }

    public Base parseBase() {
        Base base;
        if (lexer.getCurToken().equals("(")) {
            lexer.next();
            base = parseExpr();
            lexer.next();
        } else if (lexer.getCurToken().equals("x")) {
            base = parseVar();
        } else {
            base = parseNum();
        }
        return base;
    }

    public Var parseVar() {
        Var var = new Var();
        lexer.next();
        return var;
    }

    public Num parseNum() {
        Num num;
        if (lexer.getCurToken().equals("-")) {
            lexer.next();
            num = new Num(new BigInteger("-" + lexer.getCurToken()));
        } else {
            if (lexer.getCurToken().equals("+")) {
                lexer.next();
            }
            num = new Num(new BigInteger(lexer.getCurToken()));
        }
        lexer.next();
        return num;
    }

    public int parseExponent() {
        int exponent = 1;
        if (lexer.getCurToken().equals("^")) {
            lexer.next();
            if (lexer.getCurToken().equals("+")) {
                lexer.next();
            }
            exponent = Integer.parseInt(lexer.getCurToken());
            lexer.next();
        }
        return exponent;
    }
}