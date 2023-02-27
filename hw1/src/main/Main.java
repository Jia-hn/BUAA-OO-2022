package main;

import com.oocourse.spec1.ExprInput;
import com.oocourse.spec1.ExprInputMode;
import expr.Expr;
import parser.Lexer;
import parser.Parser;
import simplifiedexpr.SimplifiedExpr;

public class Main {
    public static void main(String[] arg) {
        ExprInput exprInput = new ExprInput(ExprInputMode.NormalMode);
        String input = exprInput.readLine();
        input = input.replaceAll("\\s", "");
        input = input.replaceAll("\\*\\*", "^");
        Parser parser = new Parser(new Lexer(input));
        Expr expr = parser.parseExpr();
        SimplifiedExpr simplifiedExpr = expr.simplify();
        System.out.println(simplifiedExpr);
    }
}