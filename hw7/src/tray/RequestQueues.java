package tray;

import tray.CustomRequest;

import java.util.ArrayList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RequestQueues {
    private final Lock lock;
    private final Condition condition;
    private final ArrayList<CustomRequest>[] requestQueues;
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

    public boolean canArrive(int switchInfo, char fromBuilding, char toBuilding) {
        return ((switchInfo >> (fromBuilding - 'A')) & 1) +
                ((switchInfo >> (toBuilding - 'A')) & 1) == 2;
    }

    public CustomRequest getRequest(int direction) {
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

    public void addRequest(int index, CustomRequest request) {
        lock.lock();
        try {
            requestQueues[index].add(request);
            condition.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public CustomRequest getAndRemoveRequest(int index, int direction) {
        lock.lock();
        try {
            ArrayList<CustomRequest> requestQueue = requestQueues[index];
            CustomRequest request;
            for (int i = 0; i < requestQueue.size(); i++) {
                request = requestQueue.get(i);
                if (request.getRequestDirection() == direction) {
                    requestQueue.remove(request);
                    return request;
                }
            }
            return null;
        } finally {
            lock.unlock();
        }
    }

    public CustomRequest getAndRemoveRequest(int index, int direction, int switchInfo) {
        lock.lock();
        try {
            ArrayList<CustomRequest> requestQueue = requestQueues[index];
            CustomRequest request;
            for (int i = 0; i < requestQueue.size(); i++) {
                request = requestQueue.get(i);
                if (request.getRequestDirection() == direction && canArrive(switchInfo,
                        request.getFromBuilding(), request.getToBuilding())) {
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
            ArrayList<CustomRequest> requestQueue = requestQueues[index];
            for (CustomRequest request : requestQueue) {
                if (request.getRequestDirection() == direction) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public boolean existRequest(int index, int direction, int switchInfo) {
        lock.lock();
        try {
            ArrayList<CustomRequest> requestQueue = requestQueues[index];
            for (CustomRequest request : requestQueue) {
                if (request.getRequestDirection() == direction && canArrive(switchInfo,
                        request.getFromBuilding(), request.getToBuilding())) {
                    return true;
                }
            }
            return false;
        } finally {
            lock.unlock();
        }
    }

    public int getCnt(int index) {
        lock.lock();
        try {
            return requestQueues[index].size();
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
