package task4;

import java.util.ArrayList;

public class Tray {
    private final ArrayList<Integer> numbers;
    private boolean isEnd;

    public Tray() {
        numbers = new ArrayList<>();
        isEnd = false;
    }

    public synchronized void addNumber(Integer number) {
        numbers.add(number);
        notifyAll();
    }

    public synchronized Integer getNumber() {
        if (numbers.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (numbers.isEmpty()) {
            return null;
        }
        Integer number = numbers.get(0);
        numbers.remove(0);
        notifyAll();
        return number;
    }

    public synchronized boolean isEnd() {
        notifyAll();
        return isEnd;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public synchronized boolean isEmpty() {
        notifyAll();
        return numbers.isEmpty();
    }
}
