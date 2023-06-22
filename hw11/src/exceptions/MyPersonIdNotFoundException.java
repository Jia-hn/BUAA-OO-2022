package exceptions;

import com.oocourse.spec3.exceptions.PersonIdNotFoundException;

import java.util.HashMap;

public class MyPersonIdNotFoundException extends PersonIdNotFoundException {
    private static int totalCount = 0;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();
    private final int id;

    public MyPersonIdNotFoundException(int id) {
        this.id = id;
        totalCount++;
        if (COUNTS.containsKey(id)) {
            COUNTS.put(id, COUNTS.get(id) + 1);
        } else {
            COUNTS.put(id, 1);
        }
    }

    public void print() {
        System.out.println("pinf-" + totalCount + ", " + id + "-" + COUNTS.get(id));
    }
}
