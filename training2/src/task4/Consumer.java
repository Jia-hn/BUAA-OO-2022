package task4;

public class Consumer extends Thread {
    private final Tray tray;

    public Consumer(Tray tray) {
        this.tray = tray;
    }

    @Override
    public void run() {
        while (true) {
            if (tray.isEnd() && tray.isEmpty()) {
                return;
            }
            Integer number = tray.getNumber();
            if (number == null) {
                continue;
            }
            System.out.println("task4.Consumer get:" + number);
        }
    }
}
