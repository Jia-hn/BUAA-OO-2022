package simplifiedexpr;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Objects;

public class SimplifiedExpr {
    private final HashMap<SimplifiedTerm, BigInteger> simplifiedTerms;

    public SimplifiedExpr() {
        simplifiedTerms = new HashMap<>();
    }

    public SimplifiedExpr(SimplifiedExpr simplifiedExpr) {
        simplifiedTerms = new HashMap<>();
        for (SimplifiedTerm simplifiedTerm : simplifiedExpr.simplifiedTerms.keySet()) {
            simplifiedTerms.put(new SimplifiedTerm(simplifiedTerm), new
                    BigInteger(String.valueOf(simplifiedExpr.simplifiedTerms.get(simplifiedTerm))));
        }
    }

    public HashMap<SimplifiedTerm, BigInteger> getSimplifiedTerms() {
        return simplifiedTerms;
    }

    public BigInteger get(SimplifiedTerm simplifiedTerm) {
        return simplifiedTerms.get(simplifiedTerm);
    }

    public void put(SimplifiedTerm simplifiedTerm, BigInteger coefficient) {
        if (simplifiedTerms.containsKey(simplifiedTerm)) {
            simplifiedTerms.put(simplifiedTerm, simplifiedTerms.get(simplifiedTerm).add(coefficient));
        } else {
            simplifiedTerms.put(simplifiedTerm, coefficient);
        }
        if (simplifiedTerms.get(simplifiedTerm).equals(BigInteger.ZERO)) {
            simplifiedTerms.remove(simplifiedTerm);
        }
    }

    public static SimplifiedExpr one() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        simplifiedExpr.put(new SimplifiedTerm(), BigInteger.ONE);
        return simplifiedExpr;
    }

    public static SimplifiedExpr two() {
        SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
        simplifiedExpr.put(new SimplifiedTerm(), new BigInteger("2"));
        return simplifiedExpr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplifiedExpr that = (SimplifiedExpr) o;
        return Objects.equals(simplifiedTerms, that.simplifiedTerms);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simplifiedTerms);
    }

    public SimplifiedExpr add(SimplifiedExpr augend) {
        SimplifiedExpr result = new SimplifiedExpr();
        for (SimplifiedTerm simplifiedTerm : this.simplifiedTerms.keySet()) {
            result.put(simplifiedTerm, this.get(simplifiedTerm));
        }
        for (SimplifiedTerm simplifiedTerm : augend.simplifiedTerms.keySet()) {
            result.put(simplifiedTerm, augend.get(simplifiedTerm));
        }
        return result;
    }

    public SimplifiedExpr multiply(SimplifiedExpr multiplicand) {
        SimplifiedExpr result = new SimplifiedExpr();
        for (SimplifiedTerm simplifiedTerm1 : this.simplifiedTerms.keySet()) {
            for (SimplifiedTerm simplifiedTerm2 : multiplicand.simplifiedTerms.keySet()) {
                BigInteger bigInteger = this.get(simplifiedTerm1).
                        multiply(multiplicand.get(simplifiedTerm2));
                SimplifiedTerm simplifiedTerm = simplifiedTerm1.multiply(simplifiedTerm2);
                SimplifiedExpr simplifiedExpr = new SimplifiedExpr();
                simplifiedExpr.put(simplifiedTerm, bigInteger);
                result = result.add(simplifiedExpr);
            }
        }
        return result;
    }

    public SimplifiedExpr negate() {
        SimplifiedExpr result = new SimplifiedExpr();
        for (SimplifiedTerm simplifiedTerm : simplifiedTerms.keySet()) {
            result.put(simplifiedTerm, get(simplifiedTerm).negate());
        }
        return result;
    }

    public SimplifiedExpr pow(int n) {
        SimplifiedExpr result = SimplifiedExpr.one();
        for (int i = 0; i < n; i++) {
            result = result.multiply(this);
        }
        return result;
    }

    private void sumOfSquares() {
        for (SimplifiedTerm simplifiedTerm1 : simplifiedTerms.keySet()) {
            SimplifiedTerm tempTerm1 = new SimplifiedTerm(simplifiedTerm1);
            for (SimplifiedBase simplifiedBase1 : tempTerm1.getSimplifiedBases().keySet()) {
                if (!(simplifiedBase1 instanceof SimplifiedFunc &&
                        ((SimplifiedFunc) simplifiedBase1).getFuncName().equals("sin")
                        && tempTerm1.get(simplifiedBase1) >= 2)) {
                    continue;
                }
                SimplifiedBase simplifiedBase2 = new SimplifiedFunc("cos", new
                        SimplifiedExpr(((SimplifiedFunc) simplifiedBase1).getSimplifiedExpr()));
                tempTerm1.put(simplifiedBase1, -2);
                for (SimplifiedTerm simplifiedTerm2 : simplifiedTerms.keySet()) {
                    SimplifiedTerm tempTerm2 = new SimplifiedTerm(simplifiedTerm2);
                    if (simplifiedTerm1 == simplifiedTerm2) {
                        continue;
                    }
                    if (tempTerm2.getSimplifiedBases().containsKey(simplifiedBase2) &&
                            tempTerm2.getSimplifiedBases().get(simplifiedBase2) >= 2) {
                        tempTerm2.put(simplifiedBase2, -2);
                        if (tempTerm1.equals(tempTerm2)) {
                            put(tempTerm1, get(simplifiedTerm2));
                            put(simplifiedTerm1, get(simplifiedTerm2).negate());
                            simplifiedTerms.remove(simplifiedTerm2);
                            return;
                        }
                    }
                }
            }
        }
    }

    private void doubleAngle() {
        for (SimplifiedTerm simplifiedTerm : simplifiedTerms.keySet()) {
            for (SimplifiedBase simplifiedBase1 : simplifiedTerm.getSimplifiedBases().keySet()) {
                if (!(simplifiedBase1 instanceof SimplifiedFunc) ||
                        ((SimplifiedFunc) simplifiedBase1).getFuncName().equals("cos")) {
                    continue;
                }
                SimplifiedBase simplifiedBase2 = new SimplifiedFunc("cos",
                        ((SimplifiedFunc) simplifiedBase1).getSimplifiedExpr());
                Integer exponent1 = simplifiedTerm.get(simplifiedBase1);
                Integer exponent2 = simplifiedTerm.get(simplifiedBase2);
                if (simplifiedTerm.getSimplifiedBases().containsKey(simplifiedBase2) && exponent1.equals(exponent2)
                        && get(simplifiedTerm).mod(new BigInteger("2").pow(exponent1)).equals(BigInteger.ZERO)) {
                    SimplifiedExpr simplifiedExpr = ((SimplifiedFunc) simplifiedBase1).
                            getSimplifiedExpr().multiply(SimplifiedExpr.two());
                    SimplifiedBase simplifiedBase3 = new SimplifiedFunc("sin", simplifiedExpr);
                    SimplifiedTerm temp = new SimplifiedTerm(simplifiedTerm);
                    temp.getSimplifiedBases().remove(simplifiedBase1);
                    temp.getSimplifiedBases().remove(simplifiedBase2);
                    temp.put(simplifiedBase3, exponent1);
                    put(temp, get(simplifiedTerm).divide(new BigInteger("2").pow(exponent1)));
                    simplifiedTerms.remove(simplifiedTerm);
                    return;
                }
            }
        }
    }

    public void optimize() {
        SimplifiedExpr old = null;
        while (old == null || !String.valueOf(old).equals(String.valueOf(this))) {
            old = new SimplifiedExpr(this);
            this.doubleAngle();
            this.sumOfSquares();
        }
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SimplifiedTerm simplifiedTerm : simplifiedTerms.keySet()) {
            if (!simplifiedTerms.get(simplifiedTerm).equals(BigInteger.ZERO)) {
                stringBuilder.append("+");
                stringBuilder.append(simplifiedTerms.get(simplifiedTerm));
                if (simplifiedTerm.getSimplifiedBases().size() != 0) {
                    stringBuilder.append("*");
                    stringBuilder.append(simplifiedTerm);
                }
            }
        }
        String ret = String.valueOf(stringBuilder);
        ret = ret.replaceAll("sin\\(x\\*x\\)", "sin(x**2)");
        ret = ret.replaceAll("cos\\(x\\*x\\)", "cos(x**2)");
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
