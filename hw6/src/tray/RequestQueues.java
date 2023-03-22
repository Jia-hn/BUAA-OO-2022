package tray;

import com.oocourse.elevator2.PersonRequest;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RequestQueues {
    private final Lock lock;
    private final Condition condition;
    private final ArrayList<PersonRequest>[] requestQueues;
    private boolean isEnd;

    public RequestQueues(int size) {
        lock = new ReentrantLock();
        condition = lock.newCondition();
        requestQueues = new ArrayList[size];
        for (int i = 1; i < size; i++) {
            requestQueues[i] = new ArrayList<>();
        }
        isEnd = false;
    }

    public int getRequestDirection(PersonRequest request) {
        if (request.getToFloor() == request.getFromFloor()) {
            return (request.getToBuilding() - request.getFromBuilding() +
                    requestQueues.length - 1) % (requestQueues.length - 1) <= 2 ? 1 : -1;
        } else {
            return (request.getToFloor() - request.getFromFloor()) /
                    Math.abs(request.getToFloor() - request.getFromFloor());
        }
    }

    public PersonRequest getRequest(int direction) {
        lock.lock();
        try {
            if (isEmpty()) {
                try {
                    condition.await();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (isEmpty()) {
                return null;
            }
            if (direction == 1) {
                for (int i = requestQueues.length - 1; i >= 1; i--) {
                    if (!requestQueues[i].isEmpty()) {
                        return requestQueues[i].get(0);
                    }
                }
            } else {
                for (int i = 1; i < requestQueues.length; i++) {
                    if (!requestQueues[i].isEmpty()) {
                        return requestQueues[i].get(0);
                    }
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public void addRequest(int index, PersonRequest request) {
        lock.lock();
        try {
            requestQueues[index].add(request);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public PersonRequest getAndRemoveRequest(int index, int direction) {
        lock.lock();
        try {
            ArrayList<PersonRequest> requestQueue = requestQueues[index];
            PersonRequest request;
            for (int i = 0; i < requestQueue.size(); i++) {
                request = requestQueue.get(i);
                if (getRequestDirection(request) == direction) {
                    requestQueue.remove(request);
                    return request;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public boolean existRequest(int index, int direction) {
        lock.lock();
        try {
            ArrayList<PersonRequest> requestQueue = requestQueues[index];
            for (PersonRequest request : requestQueue) {
                if (getRequestDirection(request) == direction) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getCnt(int direction) {
        lock.lock();
        try {
            int cnt = 0;
            for (int i = 1; i < requestQueues.length; i++) {
                for (PersonRequest request : requestQueues[i]) {
                    if (getRequestDirection(request) == direction) {
                        cnt++;
                    }
                }
            }
            return cnt;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            for (int i = 1; i < requestQueues.length; i++) {
                if (!requestQueues[i].isEmpty()) {
                    return false;
                }
            }
            return true;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEnd() {
        lock.lock();
        try {
            return isEnd;
        } finally {
            lock.unlock();
        }
    }

    public void setEnd(boolean end) {
        lock.lock();
        try {
            isEnd = end;
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }
}
