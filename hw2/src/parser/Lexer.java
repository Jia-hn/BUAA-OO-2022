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

    private String getFuncName() {
        char c1 = input.charAt(pos);
        char c2 = input.charAt(pos + 1);
        StringBuilder funcName = new StringBuilder();
        if ("fgh".indexOf(c1) != -1 || c2 == 'u') {
            if (c2 == 'u') {
                funcName.append(input, pos, pos + 4);
                pos += 4;
            } else {
                funcName.append(input, pos, pos + 2);
                pos += 2;
            }
            for (int temp = 1; temp > 0; pos++) {
                funcName.append(input.charAt(pos));
                if (input.charAt(pos) == ')') {
                    temp--;
                }
                if (input.charAt(pos) == '(') {
                    temp++;
                }
            }
        } else {
            for (; pos < input.length() && Character.isLowerCase(input.charAt(pos)); pos++) {
                funcName.append(input.charAt(pos));
            }
        }
        return String.valueOf(funcName);
    }

    public void next() {
        if (pos == input.length()) {
            return;
        }
        char c = input.charAt(pos);
        if (Character.isDigit(c)) {
            curToken = getNumber();
        } else if ("+-*^()x".indexOf(c) != -1) {
            curToken = String.valueOf(c);
            pos++;
        } else if ("scfgh".indexOf(c) != -1) {
            curToken = getFuncName();
        }
    }
}
