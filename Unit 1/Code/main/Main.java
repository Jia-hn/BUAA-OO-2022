package main;

import com.oocourse.spec3.ExprInput;
import com.oocourse.spec3.ExprInputMode;
import expr.Expr;
import parser.Lexer;
import parser.Parser;
import java.util.HashMap;

public class Main {
    private static HashMap<Character, String> functions;

    public static HashMap<Character, String> getFunctions() {
        return functions;
    }

    public static void main(String[] arg) {
        ExprInput exprInput = new ExprInput(ExprInputMode.NormalMode);
        int n = exprInput.getCount();
        functions = new HashMap<>();
        for (int i = 0; i < n; i++) {
            String string = exprInput.readLine();
            string = string.replaceAll("\\s", "");
            string = string.replaceAll("\\*\\*", "^");
            functions.put(string.charAt(0), string);
        }
        String input = exprInput.readLine();
        input = input.replaceAll("\\s", "");
        input = input.replaceAll("\\*\\*", "^");
        Parser parser = new Parser(new Lexer(input));
        Expr expr = parser.parseExpr().simplify().optimize();
        System.out.println(expr);
    }
}
