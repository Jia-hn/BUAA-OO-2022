package task4;

public class Producer extends Thread {
    private final Tray tray;

    public Producer(Tray tray) {
        this.tray = tray;
    }

    @Override
    public void run() {
        for (int i = 1; i <= 10; i++) {
            tray.addNumber(i);
            System.out.println("task4.Producer put:" + i);
            try {
                sleep((int) (Math.random() * 100));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        tray.setEnd(true);
    }
}
