package exceptions;

import com.oocourse.spec2.exceptions.EqualGroupIdException;

import java.util.HashMap;

public class MyEqualGroupIdException extends EqualGroupIdException {
    private static int totalCount = 0;
    private static final HashMap<Integer, Integer> COUNTS = new HashMap<>();
    private final int id;

    public MyEqualGroupIdException(int id) {
        this.id = id;
        totalCount++;
        if (COUNTS.containsKey(id)) {
            COUNTS.put(id, COUNTS.get(id) + 1);
        } else {
            COUNTS.put(id, 1);
        }
    }

    public void print() {
        System.out.println("egi-" + totalCount + ", " + id + "-" + COUNTS.get(id));
    }
}
