import java.util.ArrayList;

public class RequestQueue {
    private final ArrayList<Request> requests;
    private boolean isEnd;

    public RequestQueue() {
        requests = new ArrayList<>();
        this.isEnd = false;
    }

    public synchronized void addRequest(Request request) {
        requests.add(request);
        notifyAll();
    }

    public synchronized Request getOneRequest() {
        if (requests.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        if (requests.isEmpty()) {
            return null;
        }
        Request request = requests.get(0);
        requests.remove(0);
        return request;
    }

    public synchronized void setEnd(boolean isEnd) {
        this.isEnd = isEnd;
        notifyAll();
    }

    public boolean isEnd() {
        return isEnd;
    }

    public boolean isEmpty() {
        return requests.isEmpty();
    }
}
