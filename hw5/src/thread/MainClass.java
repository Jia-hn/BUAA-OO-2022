package thread;

import com.oocourse.TimableOutput;
import tray.RequestQueues;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        RequestQueues[] waitingQueues = new RequestQueues[6];
        for (int i = 1; i < 6; i++) {
            waitingQueues[i] = new RequestQueues(11);
        }
        Input input = new Input(waitingQueues);
        input.start();
    }
}
