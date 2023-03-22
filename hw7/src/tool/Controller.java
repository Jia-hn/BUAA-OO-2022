package tool;

import com.oocourse.elevator3.ElevatorRequest;
import com.oocourse.elevator3.Request;
import thread.TransverseElevator;
import thread.VerticalElevator;
import tray.CustomRequest;
import tray.RequestCounter;
import tray.RequestQueues;

import java.util.Vector;

public class Controller {
    private static final Controller CONTROLLER = new Controller();
    private final Vector<TransverseElevator>[] transverseElevators;
    private final RequestQueues[] waitingQueues1;
    private final RequestQueues[] waitingQueues2;
    private final RequestCounter requestCounter;

    public static Controller getInstance() {
        return CONTROLLER;
    }

    public Controller() {
        waitingQueues1 = new RequestQueues[6];
        waitingQueues2 = new RequestQueues[11];
        for (int i = 1; i < 6; i++) {
            waitingQueues1[i] = new RequestQueues(11);
        }
        for (int i = 1; i < 11; i++) {
            waitingQueues2[i] = new RequestQueues(6);
        }
        transverseElevators = new Vector[11];
        for (int i = 1; i < 11; i++) {
            transverseElevators[i] = new Vector<>();
        }
        for (int i = 1; i < 6; i++) {
            VerticalElevator verticalElevator = new VerticalElevator(i, (char) (i - 1 + 'A'), 8,
                    600, waitingQueues1[i]);
            verticalElevator.start();
        }
        TransverseElevator transverseElevator = new TransverseElevator(6, 1, 8,
                600, 31, waitingQueues2[1]);
        transverseElevator.start();
        transverseElevators[1].add(transverseElevator);
        requestCounter = new RequestCounter();
    }

    public boolean canArrive(int floor, char fromBuilding, char toBuilding) {
        for (TransverseElevator transverseElevator : transverseElevators[floor]) {
            int switchInfo = transverseElevator.getSwitchInfo();
            if (((switchInfo >> (fromBuilding - 'A')) & 1) +
                    ((switchInfo >> (toBuilding - 'A')) & 1) == 2) {
                return true;
            }
        }
        return false;
    }

    public void addRequest(Request request) {
        if (request instanceof CustomRequest) {
            CustomRequest customRequest = (CustomRequest) request;
            char fromBuilding = customRequest.getFromBuilding();
            int fromFloor = customRequest.getFromFloor();
            int toFloor = customRequest.getToFloor();
            char finalBuilding = customRequest.getFinalBuilding();
            int finalFloor = customRequest.getFinalFloor();
            if (fromBuilding == finalBuilding) {
                customRequest.setToBuilding(finalBuilding);
                customRequest.setToFloor(finalFloor);
                waitingQueues1[fromBuilding - 'A' + 1].addRequest(fromFloor, customRequest);
            } else {
                int minDistance = 100;
                int minCnt = 10;
                if (canArrive(fromFloor, fromBuilding, finalBuilding)) {
                    toFloor = fromFloor;
                } else if (canArrive(finalFloor, fromBuilding, finalBuilding)) {
                    toFloor = finalFloor;
                } else {
                    for (int i = 1; i < 11; i++) {
                        if (canArrive(i, fromBuilding, finalBuilding)) {
                            int distance = Math.abs(i - fromFloor) + Math.abs(i - finalFloor);
                            int cnt = waitingQueues1[fromBuilding - 'A' + 1].getCnt(i);
                            if (distance < minDistance || (distance
                                    == minDistance && cnt < minCnt)) {
                                minDistance = distance;
                                minCnt = cnt;
                                toFloor = i;
                            }
                        }
                    }
                }
                customRequest.setToFloor(toFloor);
                if (fromFloor == toFloor) {
                    customRequest.setToBuilding(finalBuilding);
                    waitingQueues2[fromFloor].addRequest(fromBuilding - 'A' + 1, customRequest);
                } else {
                    customRequest.setToBuilding(fromBuilding);
                    waitingQueues1[fromBuilding - 'A' + 1].addRequest(fromFloor, customRequest);
                }
            }
        } else {
            ElevatorRequest elevatorRequest = (ElevatorRequest) request;
            char building = elevatorRequest.getBuilding();
            int floor = elevatorRequest.getFloor();
            int capacity = elevatorRequest.getCapacity();
            int speed = (int) (elevatorRequest.getSpeed() * 1000);
            int switchInfo = elevatorRequest.getSwitchInfo();
            if (elevatorRequest.getType().equals("building")) {
                new VerticalElevator(elevatorRequest.getElevatorId(), building, capacity,
                        speed, waitingQueues1[building - 'A' + 1]).start();
            } else {
                TransverseElevator transverseElevator = new TransverseElevator(elevatorRequest.
                        getElevatorId(), floor, capacity, speed, switchInfo, waitingQueues2[floor]);
                transverseElevator.start();
                transverseElevators[floor].add(transverseElevator);
            }
        }
    }

    public void setEnd() {
        for (int i = 1; i < 6; i++) {
            waitingQueues1[i].setEnd(true);
        }
        for (int i = 1; i < 11; i++) {
            waitingQueues2[i].setEnd(true);
        }
    }

    public void add() {
        requestCounter.add();
    }

    public void subtract() {
        requestCounter.subtract();
    }

    public int getCounter() {
        return requestCounter.getCounter();
    }
}
