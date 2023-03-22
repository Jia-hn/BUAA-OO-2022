package thread;

import com.oocourse.elevator3.ElevatorInput;
import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;
import tool.Controller;
import tray.CustomRequest;

public class Input extends Thread {
    @Override
    public void run() {
        ElevatorInput elevatorInput = new ElevatorInput(System.in);
        while (true) {
            Request request = elevatorInput.nextRequest();
            if (request == null) {
                try {
                    elevatorInput.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            if (request instanceof PersonRequest) {
                Controller.getInstance().addRequest(new CustomRequest((PersonRequest) request));
                Controller.getInstance().add();
            } else {
                Controller.getInstance().addRequest(request);
            }
        }
        while (true) {
            if (Controller.getInstance().getCounter() == 0) {
                Controller.getInstance().setEnd();
                break;
            }
        }
    }
}
