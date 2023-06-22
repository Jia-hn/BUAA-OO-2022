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
        set.put(x, -1);
    }

    public int find(int x) {
        if (set.get(x) < 0) {
            return x;
        } else {
            int y = find(set.get(x));
            set.put(x, y);
            return y;
        }
    }

    public void union(int x1, int x2) {
        int root1;
        int root2;
        root1 = find(x1);
        root2 = find(x2);
        if (root1 != root2) {
            if (set.get(root2) < set.get(root1)) {
                set.put(root2, set.get(root1) + set.get(root2));
                set.put(root1, root2);
            } else {
                set.put(root1, set.get(root1) + set.get(root2));
                set.put(root2, root1);
            }
        }
    }
}
