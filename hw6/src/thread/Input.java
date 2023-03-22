package thread;

import com.oocourse.elevator2.ElevatorInput;
import com.oocourse.elevator2.ElevatorRequest;
import com.oocourse.elevator2.PersonRequest;
import com.oocourse.elevator2.Request;
import tray.RequestQueues;

public class Input extends Thread {
    private final RequestQueues[] waitingQueues1;
    private final RequestQueues[] waitingQueues2;

    public Input(RequestQueues[] waitingQueues1, RequestQueues[] waitingQueues2) {
        this.waitingQueues1 = waitingQueues1;
        this.waitingQueues2 = waitingQueues2;
    }

    @Override
    public void run() {
        for (int i = 1; i < 6; i++) {
            new VerticalElevator(i, (char) (i - 1 + 'A'), waitingQueues1[i]).start();
        }
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                for (int i = 1; i < 6; i++) {
                    waitingQueues1[i].setEnd(true);
                }
                for (int i = 1; i < 11; i++) {
                    waitingQueues2[i].setEnd(true);
                }
                try {
                    elevatorInput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            if (request instanceof PersonRequest) {
                PersonRequest personRequest = (PersonRequest) request;
                if (personRequest.getFromBuilding() == personRequest.getToBuilding()) {
                    waitingQueues1[personRequest.getFromBuilding() - 'A' + 1].
                            addRequest(personRequest.getFromFloor(), personRequest);
                } else {
                    waitingQueues2[personRequest.getFromFloor()].addRequest(
                            personRequest.getFromBuilding() - 'A' + 1, personRequest);
                }
            } else if (request instanceof ElevatorRequest) {
                ElevatorRequest elevatorRequest = (ElevatorRequest) request;
                char building = elevatorRequest.getBuilding();
                int floor = elevatorRequest.getFloor();
                if (elevatorRequest.getType().equals("building")) {
                    new VerticalElevator(elevatorRequest.getElevatorId(),
                            building, waitingQueues1[building - 'A' + 1]).start();
                } else {
                    new TransverseElevator(elevatorRequest.getElevatorId(),
                            floor, waitingQueues2[floor]).start();
                }
            }
        }
    }
}
