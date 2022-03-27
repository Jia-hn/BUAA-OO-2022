package parser;

import expr.Expr;
import expr.Term;
import expr.Base;
import expr.Func;
import expr.Num;
import expr.Var;
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
        boolean flag = true;
        while (flag || lexer.getCurToken().equals("+") || lexer.getCurToken().equals("-")) {
            int sign = 1;
            if (lexer.getCurToken().equals("+")) {
                lexer.next();
            } else if (lexer.getCurToken().equals("-")) {
                lexer.next();
                sign *= -1;
            }
            if (lexer.getCurToken().equals("+")) {
                lexer.next();
            } else if (lexer.getCurToken().equals("-")) {
                lexer.next();
                sign *= -1;
            }
            if (sign == 1) {
                expr.put(parseTerm(), BigInteger.ONE);
            } else {
                expr.put(parseTerm(), BigInteger.ONE.negate());
            }
            flag = false;
        }
        return expr;
    }

    public Term parseTerm() {
        Term term = new Term();
        term.put(parseBase(), parseExponent());
        while (lexer.getCurToken().equals("*")) {
            lexer.next();
            term.put(parseBase(), parseExponent());
        }
        return term;
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
        int sign = 1;
        if (lexer.getCurToken().equals("-")) {
            sign *= -1;
        }
        if (lexer.getCurToken().equals("+") || lexer.getCurToken().equals("-")) {
            lexer.next();
        }
        if (sign == -1) {
            num = new Num(new BigInteger("-" + lexer.getCurToken()));
        } else {
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
