package exceptions;

import com.oocourse.spec2.exceptions.EqualMessageIdException;

import java.util.HashMap;

public class MyEqualMessageIdException extends EqualMessageIdException {
    private static int totalCount = 0;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();
    private final int id;

    public MyEqualMessageIdException(int id) {
        this.id = id;
        totalCount++;
        if (COUNTS.containsKey(id)) {
            COUNTS.put(id, COUNTS.get(id) + 1);
        } else {
            COUNTS.put(id, 1);
        }
    }

    public void print() {
        System.out.println("emi-" + totalCount + ", " + id + "-" + COUNTS.get(id));
    }
}