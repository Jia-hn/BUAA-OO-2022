package thread;

import com.oocourse.elevator1.ElevatorInput;
import com.oocourse.elevator1.PersonRequest;
import tray.RequestQueues;

public class Input extends Thread {
    private final RequestQueues[] waitingQueues;

    public Input(RequestQueues[] waitingQueues) {
        this.waitingQueues = waitingQueues;
    }

    @Override
    public void run() {
        for (int i = 1; i < 6; i++) {
            Elevator elevator = new Elevator(i, (char)(i - 1 + 'A'), waitingQueues[i]);
            elevator.start();
        }
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            PersonRequest request = elevatorInput.nextPersonRequest();
            if (request == null) {
                for (int i = 1; i < 6; i++) {
                    waitingQueues[i].setEnd(true);
                }
                try {
                    elevatorInput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return;
            }
            waitingQueues[request.getFromBuilding() - 'A' + 1].
                    addRequest(request.getFromFloor(),request);
        }
    }
}
