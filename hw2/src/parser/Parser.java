package parser;

import expr.Expr;
import expr.Term;
import expr.Factor;
import expr.Base;
import expr.Var;
import expr.Num;
import expr.Func;
import main.Main;
import java.math.BigInteger;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        } else if (lexer.getCurToken().equals("sin") || lexer.getCurToken().equals("cos")) {
            base = parseFunc();
        } else if (lexer.getCurToken().equals("x")) {
            base = parseVar();
        } else if ("fgh".indexOf(lexer.getCurToken().charAt(0)) != -1) {
            base = parseCustomFunc();
        } else if (lexer.getCurToken().startsWith("sum")) {
            base = parseSumFunc();
        } else {
            base = parseNum();
        }
        return base;
    }

    public Func parseFunc() {
        Func func;
        String funcName = lexer.getCurToken();
        lexer.next();
        lexer.next();
        func = new Func(funcName, parseExpr());
        lexer.next();
        return func;
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

    public Expr parseCustomFunc() {
        Pattern pattern1 = Pattern.compile("\\((.*?)\\)");
        String function = Main.getFunctions().get(lexer.getCurToken().charAt(0));
        Matcher matcher1 = pattern1.matcher(function);
        matcher1.find();
        String[] parameters = matcher1.group(1).split(",");
        Pattern pattern2 = Pattern.compile("\\((.*)\\)");
        Matcher matcher2 = pattern2.matcher(lexer.getCurToken());
        matcher2.find();
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0, temp = 0; i < matcher2.group(1).length(); i++) {
            if (matcher2.group(1).charAt(i) == ',' && temp == 0) {
                stringBuilder.append('.');
            } else {
                stringBuilder.append(matcher2.group(1).charAt(i));
            }
            if (matcher2.group(1).charAt(i) == '(') {
                temp++;
            }
            if (matcher2.group(1).charAt(i) == ')') {
                temp--;
            }
        }
        String[] arguments = stringBuilder.toString().split("\\.");
        TreeMap<String, String> treeMap = new TreeMap<>();
        for (int i = 0; i < parameters.length; i++) {
            treeMap.put(parameters[i], arguments[i]);
        }
        function = function.split("=")[1];
        for (String string : treeMap.keySet()) {
            function = function.replace(string, "(" + treeMap.get(string) + ")");
        }
        lexer.next();
        Parser parser = new Parser(new Lexer(function));
        return parser.parseExpr();
    }

    public Expr parseSumFunc() {
        Pattern pattern3 = Pattern.compile("\\((.*)\\)");
        Matcher matcher = pattern3.matcher(lexer.getCurToken());
        matcher.find();
        String[] strings = matcher.group(1).split(",");
        StringBuilder stringBuilder = new StringBuilder();
        BigInteger start = new BigInteger(strings[1]);
        BigInteger end = new BigInteger(strings[2]);
        strings[3] = strings[3].replace("sin", "S");
        for (BigInteger i = new BigInteger(String.valueOf(start)); i.compareTo(end) <= 0;
             i = i.add(BigInteger.ONE)) {
            stringBuilder.append("+");
            stringBuilder.append(strings[3].replace("i", "(" + i + ")"));
        }
        strings[3] = strings[3].replace("S", "sin");
        stringBuilder = new StringBuilder(String.valueOf(stringBuilder).replace("S", "sin"));
        if (stringBuilder.length() == 0) {
            stringBuilder.append("0");
        }
        lexer.next();
        Parser parser = new Parser(new Lexer(String.valueOf(stringBuilder)));
        return parser.parseExpr();
    }
}
