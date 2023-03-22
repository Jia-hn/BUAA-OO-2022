package tray;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RequestCounter {
    private final Lock lock;
    private final Condition condition;
    private int counter;

    public RequestCounter() {
        lock = new ReentrantLock();
        condition = lock.newCondition();
        counter = 0;
    }

    public void add() {
        lock.lock();
        try {
            counter++;
        } finally {
            lock.unlock();
        }
    }

    public void subtract() {
        lock.lock();
        try {
            counter--;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public int getCounter() {
        lock.lock();
        try {
            if (counter == 0) {
                return counter;
            }
            try {
                condition.await();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return counter;
        } finally {
            lock.unlock();
        }
    }
}
