package thread;

import com.oocourse.elevator2.PersonRequest;
import tool.Output;
import tray.RequestQueues;

import java.util.ArrayList;

public class TransverseElevator extends Thread {
    private final int id;
    private char curBuilding;
    private final int curFloor;
    private int direction;
    private final RequestQueues waitingQueues;
    private final ArrayList<PersonRequest> processingQueue;
    private long lastTime;
    private long curTime;

    public TransverseElevator(int id, int curFloor, RequestQueues waitingQueues) {
        this.id = id;
        this.curBuilding = 'A';
        this.curFloor = curFloor;
        this.direction = 1;
        this.lastTime = System.currentTimeMillis();
        this.waitingQueues = waitingQueues;
        this.processingQueue = new ArrayList<>();
    }

    public void move() {
        curTime = System.currentTimeMillis();
        try {
            sleep(200 + lastTime - curTime > 0 ? 200 + lastTime - curTime : 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        curBuilding = (char) ((curBuilding - 'A' + direction + 5) % 5 + 'A');
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
        while (processingQueue.size() < 6 && (request = waitingQueues.
                getAndRemoveRequest(curBuilding - 'A' + 1, direction)) != null) {
            processingQueue.add(request);
            Output.println("IN-" + request.getPersonId()
                    + "-" + curBuilding + "-" + curFloor + "-" + id);
        }
    }

    public void out() {
        PersonRequest request;
        for (int i = 0; i < processingQueue.size(); i++) {
            request = processingQueue.get(i);
            if (request.getToBuilding() == curBuilding) {
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
            if (request.getToBuilding() == curBuilding) {
                return true;
            }
        }
        if (processingQueue.size() == 6) {
            return false;
        }
        return waitingQueues.existRequest(curBuilding - 'A' + 1, direction);
    }

    @Override
    public void run() {
        while (true) {
            if (waitingQueues.isEnd() && waitingQueues.isEmpty() && processingQueue.isEmpty()) {
                return;
            }
            if (processingQueue.isEmpty()) {
                PersonRequest request = waitingQueues.getRequest(direction);
                if (request == null) {
                    continue;
                }
                if (waitingQueues.getCnt(1) >= waitingQueues.getCnt(-1)) {
                    direction = 1;
                } else {
                    direction = -1;
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
