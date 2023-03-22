package tool;

import com.oocourse.TimableOutput;

public class Output {
    public static synchronized void println(String msg) {
        TimableOutput.println(msg);
    }
}
