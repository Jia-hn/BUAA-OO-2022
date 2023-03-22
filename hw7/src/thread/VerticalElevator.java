package thread;

import tool.Controller;
import tool.Output;
import tray.CustomRequest;
import tray.RequestQueues;

import java.util.ArrayList;

public class VerticalElevator extends Thread {
    private final int id;
    private final char curBuilding;
    private int curFloor;
    private int direction;
    private final int capacity;
    private final int speed;
    private final RequestQueues waitingQueues;
    private final ArrayList<CustomRequest> processingQueue;
    private long lastTime;
    private long curTime;

    public VerticalElevator(int id, char curBuilding, int capacity, int speed,
                            RequestQueues waitingQueues) {
        this.id = id;
        this.curBuilding = curBuilding;
        this.curFloor = 1;
        this.direction = 1;
        this.capacity = capacity;
        this.speed = speed;
        this.waitingQueues = waitingQueues;
        this.processingQueue = new ArrayList<>();
        this.lastTime = System.currentTimeMillis();
    }

    public void move() throws InterruptedException {
        curTime = System.currentTimeMillis();
        sleep(speed + lastTime - curTime > 0 ? speed + lastTime - curTime : 0);
        curFloor = curFloor + direction;
        Output.println("ARRIVE-" + curBuilding + "-" + curFloor + "-" + id);
        lastTime = System.currentTimeMillis();
    }

    public void open() {
        Output.println("OPEN-" + curBuilding + "-" + curFloor + "-" + id);
        lastTime = System.currentTimeMillis();
    }

    public void close() throws InterruptedException {
        curTime = System.currentTimeMillis();
        sleep(400 + lastTime - curTime > 0 ? 400 + lastTime - curTime : 0);
        Output.println("CLOSE-" + curBuilding + "-" + curFloor + "-" + id);
        lastTime = System.currentTimeMillis();
    }

    public void in() {
        CustomRequest request;
        while (processingQueue.size() < capacity &&
                (request = waitingQueues.getAndRemoveRequest(curFloor, direction)) != null) {
            processingQueue.add(request);
            Output.println("IN-" + request.getPersonId()
                    + "-" + curBuilding + "-" + curFloor + "-" + id);
        }
    }

    public void out() {
        CustomRequest request;
        for (int i = 0; i < processingQueue.size(); i++) {
            request = processingQueue.get(i);
            if (request.getToFloor() == curFloor) {
                Output.println("OUT-" + request.getPersonId() + "-"
                        + curBuilding + "-" + curFloor + "-" + id);
                processingQueue.remove(i);
                request.setFromBuilding(curBuilding);
                request.setFromFloor(curFloor);
                if ((request.getFromBuilding() != request.getFinalBuilding()) ||
                        (request.getFromFloor() != request.getFinalFloor())) {
                    Controller.getInstance().addRequest(request);
                } else {
                    Controller.getInstance().subtract();
                }
                i--;
            }
        }
    }

    public void inAndOut() throws InterruptedException {
        open();
        out();
        sleep(385);
        in();
        close();
    }

    public boolean needOpen() {
        for (CustomRequest request : processingQueue) {
            if (request.getToFloor() == curFloor) {
                return true;
            }
        }
        if (processingQueue.size() == capacity) {
            return false;
        }
        return waitingQueues.existRequest(curFloor, direction);
    }

    @Override
    public void run() {
        while (true) {
            if (waitingQueues.isEnd() && waitingQueues.isEmpty()
                    && processingQueue.isEmpty()) {
                return;
            }
            if (processingQueue.isEmpty()) {
                CustomRequest request = waitingQueues.getRequest(direction);
                if (request == null) {
                    continue;
                }
                if (request.getFromFloor() > curFloor) {
                    direction = 1;
                } else if (request.getFromFloor() < curFloor) {
                    direction = -1;
                } else {
                    direction = request.getRequestDirection();
                }
                try {
                    if (needOpen()) {
                        inAndOut();
                    } else {
                        move();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    move();
                    if (needOpen()) {
                        inAndOut();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
