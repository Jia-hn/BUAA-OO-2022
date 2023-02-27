package simplifiedexpr;

import java.util.HashMap;
import java.util.Objects;

public class SimplifiedTerm {
    private final HashMap<SimplifiedBase, Integer> simplifiedBases;

    public SimplifiedTerm() {
        simplifiedBases = new HashMap<>();
    }

    public SimplifiedTerm(SimplifiedTerm simplifiedTerm) {
        simplifiedBases = new HashMap<>();
        for (SimplifiedBase simplifiedBase : simplifiedTerm.simplifiedBases.keySet()) {
            if (simplifiedBase instanceof SimplifiedVar) {
                simplifiedBases.put(new SimplifiedVar((SimplifiedVar) simplifiedBase),
                        simplifiedTerm.simplifiedBases.get(simplifiedBase));
            } else if (simplifiedBase instanceof SimplifiedFunc) {
                simplifiedBases.put(new SimplifiedFunc((SimplifiedFunc) simplifiedBase),
                        simplifiedTerm.simplifiedBases.get(simplifiedBase));
            }
        }
    }

    public HashMap<SimplifiedBase, Integer> getSimplifiedBases() {
        return simplifiedBases;
    }

    public Integer get(SimplifiedBase simplifiedBase) {
        return simplifiedBases.get(simplifiedBase);
    }

    public void put(SimplifiedBase simplifiedBase, Integer exponent) {
        if (simplifiedBases.containsKey(simplifiedBase)) {
            simplifiedBases.put(simplifiedBase, simplifiedBases.get(simplifiedBase) + exponent);
        } else {
            simplifiedBases.put(simplifiedBase, exponent);
        }
        if (simplifiedBases.get(simplifiedBase).equals(0)) {
            simplifiedBases.remove(simplifiedBase);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SimplifiedTerm that = (SimplifiedTerm) o;
        return Objects.equals(simplifiedBases, that.simplifiedBases);
    }

    @Override
    public int hashCode() {
        return Objects.hash(simplifiedBases);
    }

    public SimplifiedTerm multiply(SimplifiedTerm multiplicand) {
        SimplifiedTerm result = new SimplifiedTerm();
        for (SimplifiedBase simplifiedBase : this.simplifiedBases.keySet()) {
            result.put(simplifiedBase, this.get(simplifiedBase));
        }
        for (SimplifiedBase simplifiedBase : multiplicand.simplifiedBases.keySet()) {
            result.put(simplifiedBase, multiplicand.get(simplifiedBase));
        }
        return result;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (SimplifiedBase simplifiedBase : simplifiedBases.keySet()) {
            if (stringBuilder.length() != 0) {
                stringBuilder.append("*");
            }
            stringBuilder.append(simplifiedBase);
            if (simplifiedBase instanceof SimplifiedVar &&
                    simplifiedBases.get(simplifiedBase).equals(2)) {
                stringBuilder.append("*x");
            } else if (!simplifiedBases.get(simplifiedBase).equals(1)) {
                stringBuilder.append("**");
                stringBuilder.append(simplifiedBases.get(simplifiedBase));
            }
        }
        return String.valueOf(stringBuilder);
    }
}
