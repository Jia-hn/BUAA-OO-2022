package thread;

import com.oocourse.elevator2.PersonRequest;
import tool.Output;
import tray.RequestQueues;

import java.util.ArrayList;

public class VerticalElevator extends Thread {
    private final int id;
    private final char curBuilding;
    private int curFloor;
    private int direction;
    private final RequestQueues waitingQueues;
    private final ArrayList<PersonRequest> processingQueue;
    private long lastTime;
    private long curTime;

    public VerticalElevator(int id, char curBuilding, RequestQueues waitingQueues) {
        this.id = id;
        this.curBuilding = curBuilding;
        this.curFloor = 1;
        this.direction = 1;
        this.lastTime = System.currentTimeMillis();
        this.waitingQueues = waitingQueues;
        this.processingQueue = new ArrayList<>();
    }

    public int getRequestDirection(PersonRequest request) {
        return (request.getToFloor() - request.getFromFloor()) /
                Math.abs(request.getToFloor() - request.getFromFloor());
    }

    public void move() {
        curTime = System.currentTimeMillis();
        try {
            sleep(400 + lastTime - curTime > 0 ? 400 + lastTime - curTime : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        curFloor = curFloor + direction;
        Output.println("ARRIVE-" + curBuilding + "-" + curFloor + "-" + id);
        lastTime = System.currentTimeMillis();
    }

    public void open() {
        Output.println("OPEN-" + curBuilding + "-" + curFloor + "-" + id);
        lastTime = System.currentTimeMillis();
    }

    public void close() {
        curTime = System.currentTimeMillis();
        try {
            sleep(400 + lastTime - curTime > 0 ? 400 + lastTime - curTime : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Output.println("CLOSE-" + curBuilding + "-" + curFloor + "-" + id);
        lastTime = System.currentTimeMillis();
    }

    public void in() {
        PersonRequest request;
        while (processingQueue.size() < 6 &&
                (request = waitingQueues.getAndRemoveRequest(curFloor, direction)) != null) {
            processingQueue.add(request);
            Output.println("IN-" + request.getPersonId()
                    + "-" + curBuilding + "-" + curFloor + "-" + id);
        }
    }

    public void out() {
        PersonRequest request;
        for (int i = 0; i < processingQueue.size(); i++) {
            request = processingQueue.get(i);
            if (request.getToFloor() == curFloor) {
                Output.println("OUT-" + request.getPersonId() + "-"
                        + curBuilding + "-" + curFloor + "-" + id);
                processingQueue.remove(i);
                i--;
            }
        }
    }

    public void inAndOut() {
        open();
        out();
        try {
            sleep(385);
        } catch (Exception e) {
            e.printStackTrace();
        }
        in();
        close();
    }

    public boolean needOpen() {
        for (PersonRequest request : processingQueue) {
            if (request.getToFloor() == curFloor) {
                return true;
            }
        }
        if (processingQueue.size() == 6) {
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
                PersonRequest request = waitingQueues.getRequest(direction);
                if (request == null) {
                    continue;
                }
                if (request.getFromFloor() > curFloor) {
                    direction = 1;
                } else if (request.getFromFloor() < curFloor) {
                    direction = -1;
                } else {
                    direction = getRequestDirection(request);
                }
                if (needOpen()) {
                    inAndOut();
                } else {
                    move();
                }
            } else {
                move();
                if (needOpen()) {
                    inAndOut();
                }
            }
        }
    }
}
