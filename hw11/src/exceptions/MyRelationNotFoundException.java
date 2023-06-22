package exceptions;

import com.oocourse.spec3.exceptions.RelationNotFoundException;

import java.util.HashMap;

public class MyRelationNotFoundException extends RelationNotFoundException {
    private static int totalCount = 0;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();
    private final int id1;
    private final int id2;

    public MyRelationNotFoundException(int id1, int id2) {
        this.id1 = Math.min(id1, id2);
        this.id2 = Math.max(id1, id2);
        totalCount++;
        if (COUNTS.containsKey(id1)) {
            COUNTS.put(id1, COUNTS.get(id1) + 1);
        } else {
            COUNTS.put(id1, 1);
        }
        if (COUNTS.containsKey(id2)) {
            COUNTS.put(id2, COUNTS.get(id2) + 1);
        } else {
            COUNTS.put(id2, 1);
        }
    }

    public void print() {
        System.out.println("rnf-" + totalCount + ", " + id1 + "-" +
                COUNTS.get(id1) + ", " + id2 + "-" + COUNTS.get(id2));
    }
}
