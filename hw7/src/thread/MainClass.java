package thread;

import com.oocourse.TimableOutput;

public class MainClass {
    public static void main(String[] args) {
        TimableOutput.initStartTimestamp();
        Input input = new Input();
        input.start();
    }
}
