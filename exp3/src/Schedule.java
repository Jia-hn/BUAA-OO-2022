import java.util.ArrayList;

public class Schedule extends Thread {
    private final RequestQueue waitQueue;
    private final ArrayList<RequestQueue> processingQueues;

    public Schedule(RequestQueue waitQueue
            , ArrayList<RequestQueue> processingQueues) {
        this.waitQueue = waitQueue;
        this.processingQueues = processingQueues;
    }

    @Override
    public void run() {
        while (true) {
            if (waitQueue.isEmpty() && waitQueue.isEnd()) {
                for (RequestQueue processingQueue : processingQueues) {
                    processingQueue.setEnd(true);
                }
                System.out.println("Schedule End");
                return;
            }
            Request request = waitQueue.getOneRequest();
            if (request == null) {
                continue;
            }
            if (request.getDestination().equals("Beijing")) {
                processingQueues.get(0).addRequest(request);
            } else if (request.getDestination().equals("Domestic")) {
                processingQueues.get(1).addRequest(request);
            } else {
                processingQueues.get(2).addRequest(request);
            }
        }
    }
}


