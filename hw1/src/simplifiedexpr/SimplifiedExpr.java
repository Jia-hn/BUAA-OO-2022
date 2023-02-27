package simplifiedexpr;

import java.math.BigInteger;
import java.util.Arrays;

public class SimplifiedExpr {
    private final BigInteger[] coefficients;

    public SimplifiedExpr() {
        coefficients = new BigInteger[9];
        Arrays.fill(coefficients, BigInteger.ZERO);
    }

    public BigInteger[] getCoefficients() {
        return coefficients;
    }

    public SimplifiedExpr add(SimplifiedExpr augend) {
        SimplifiedExpr result = new SimplifiedExpr();
        for (int i = 0; i < this.coefficients.length; i++) {
            result.coefficients[i] = this.coefficients[i].add(augend.coefficients[i]);
        }
        return result;
    }

    public SimplifiedExpr multiply(SimplifiedExpr multiplicand) {
        SimplifiedExpr result = new SimplifiedExpr();
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < multiplicand.coefficients.length; j++) {
                if (i + j < result.coefficients.length) {
                    result.coefficients[i + j] = result.coefficients[i + j].add(
                            this.coefficients[i].multiply(multiplicand.coefficients[j]));
                }
            }
        }
        return result;
    }

    public SimplifiedExpr negate() {
        SimplifiedExpr result = new SimplifiedExpr();
        for (int i = 0; i < this.coefficients.length; i++) {
            result.coefficients[i] = this.coefficients[i].negate();
        }
        return result;
    }

    public SimplifiedExpr pow(int n) {
        SimplifiedExpr result = new SimplifiedExpr();
        result.coefficients[0] = BigInteger.ONE;
        for (int i = 0; i < n; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        int index = -1;
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (coefficients[i].compareTo(BigInteger.ZERO) > 0) {
                stringBuilder.append("+");
                stringBuilder.append(coefficients[i]);
                stringBuilder.append("*x**");
                stringBuilder.append(i);
                index = i;
                break;
            }
        }
        for (int i = coefficients.length - 1; i >= 0; i--) {
            if (!coefficients[i].equals(new BigInteger("0")) && i != index) {
                stringBuilder.append("+");
                stringBuilder.append(coefficients[i]);
                stringBuilder.append("*x**");
                stringBuilder.append(i);
            }
        }
        String ret = String.valueOf(stringBuilder);
        ret = ret.replaceAll("x\\*\\*2", "x*x");
        ret = ret.replaceAll("x\\*\\*1", "x");
        ret = ret.replaceAll("\\*x\\*\\*0", "");
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