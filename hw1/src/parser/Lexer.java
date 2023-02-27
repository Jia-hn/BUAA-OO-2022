package parser;

public class Lexer {
    private final String input;
    private int pos;
    private String curToken;

    public Lexer(String input) {
        this.input = input;
        pos = 0;
        next();
    }

    public String getCurToken() {
        return curToken;
    }

    private String getNumber() {
        StringBuilder number = new StringBuilder();
        for (; pos < input.length() && Character.isDigit(input.charAt(pos)); pos++) {
            number.append(input.charAt(pos));
        }
        return String.valueOf(number);
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        }
        else if ("+-*^()x".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            pos++;
        }
    }
}