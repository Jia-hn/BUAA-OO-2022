public class Request {
    private final int leaveTime;
    private final int backTime;
    private final String destination;

    public Request(int leaveTime, int backTime, String destination) {
        this.leaveTime = leaveTime;
        this.backTime = backTime;
        this.destination = destination;
    }

    public int getLeaveTime() {
        return leaveTime;
    }

    public int getBackTime() {
        return backTime;
    }

    public String getDestination() {
        return destination;
    }

    public String toString() {
        return "<destination:" + destination + " FROM-" + leaveTime + "-TO-" + backTime + ">";
    }
}