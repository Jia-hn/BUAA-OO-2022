package thread;

import com.oocourse.TimableOutput;
import tray.RequestQueues;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueues[] waitingQueues1 = new RequestQueues[6];
        RequestQueues[] waitingQueues2 = new RequestQueues[11];
        for (int i = 1; i < 6; i++) {
            waitingQueues1[i] = new RequestQueues(11);
        }
        for (int i = 1; i < 11; i++) {
            waitingQueues2[i] = new RequestQueues(6);
        }
        Input input = new Input(waitingQueues1, waitingQueues2);
        input.start();
    }
}
