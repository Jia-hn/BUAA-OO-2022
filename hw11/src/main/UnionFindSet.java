package main;

import java.util.HashMap;

public class UnionFindSet {
    private final HashMap<Integer, Integer> set;

    public UnionFindSet() {
        set = new HashMap<>();
    }

    public HashMap<Integer, Integer> getSet() {
        return set;
    }

    public void put(int x) {
        set.put(x, x);
    }

    public int find(int x) {
        if (set.get(x) == x) {
            return x;
        } else {
            int y = find(set.get(x));
            set.put(x, y);
            return y;
        }
    }

    public void union(int x1, int x2) {
        set.put(find(x1), find(x2));
    }
}
