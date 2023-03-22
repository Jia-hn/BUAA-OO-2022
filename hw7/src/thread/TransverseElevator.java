package thread;

import tool.Controller;
import tool.Output;
import tray.CustomRequest;
import tray.RequestQueues;

import java.util.ArrayList;

public class TransverseElevator extends Thread {
    private final int id;
    private char curBuilding;
    private final int curFloor;
    private final int direction;
    private final int capacity;
    private final int speed;
    private final int switchInfo;
    private final RequestQueues waitingQueues;
    private final ArrayList<CustomRequest> processingQueue;
    private long lastTime;
    private long curTime;

    public TransverseElevator(int id, int curFloor, int capacity, int speed,
                              int switchInfo, RequestQueues waitingQueues) {
        this.id = id;
        this.curBuilding = 'A';
        this.curFloor = curFloor;
        this.direction = 1;
        this.capacity = capacity;
        this.speed = speed;
        this.switchInfo = switchInfo;
        this.waitingQueues = waitingQueues;
        this.processingQueue = new ArrayList<>();
        this.lastTime = System.currentTimeMillis();
    }

    public int getSwitchInfo() {
        return switchInfo;
    }

    public void move() throws InterruptedException {
        curTime = System.currentTimeMillis();
        sleep(speed + lastTime - curTime > 0 ? speed + lastTime - curTime : 0);
        curBuilding = (char) ((curBuilding - 'A' + direction + 5) % 5 + 'A');
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
        while (processingQueue.size() < capacity && (request = waitingQueues.
                getAndRemoveRequest(curBuilding - 'A' + 1, direction, switchInfo)) != null) {
            processingQueue.add(request);
            Output.println("IN-" + request.getPersonId()
                    + "-" + curBuilding + "-" + curFloor + "-" + id);
        }
    }

    public void out() {
        CustomRequest request;
        for (int i = 0; i < processingQueue.size(); i++) {
            request = processingQueue.get(i);
            if (request.getToBuilding() == curBuilding) {
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
            if (request.getToBuilding() == curBuilding) {
                return true;
            }
        }
        if (processingQueue.size() == capacity) {
            return false;
        }
        return waitingQueues.existRequest(curBuilding - 'A' + 1, direction, switchInfo);
    }

    @Override
    public void run() {
        while (true) {
            if (waitingQueues.isEnd() && waitingQueues.isEmpty() && processingQueue.isEmpty()) {
                return;
            }
            if (processingQueue.isEmpty()) {
                CustomRequest request = waitingQueues.getRequest(direction);
                if (request == null) {
                    continue;
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
