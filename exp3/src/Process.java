public class Process extends Thread {
    private final RequestQueue processingQueue;
    private final int type;

    public Process(RequestQueue processingQueue, int type) {
        this.processingQueue = processingQueue;
        this.type = type;
    }

    @Override
    public void run() {
        while (true) {
            if (processingQueue.isEnd() && processingQueue.isEmpty()) {
                System.out.println("P " + type + " over");
                return;
            }
            Request request = processingQueue.getOneRequest();
            if (request == null) {
                continue;
            }
            switch (type) {
                case 0: {
                    dealBeijingRequest(request);
                    break;
                }
                case 1: {
                    dealDomesticRequest(request);
                    break;
                }
                default: {
                    dealForeign(request);
                    break;
                }
            }
        }
    }

    private void dealBeijingRequest(Request request) {
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if ((request.getBackTime() - request.getLeaveTime()) < 24) {
            System.out.println("ALLOW: " + request);
        } else {
            System.out.println("REJECT: " + request);
        }
    }

    private void dealDomesticRequest(Request request) {
        try {
            sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if ((request.getBackTime() - request.getLeaveTime()) < 24 * 7) {
            System.out.println("ALLOW: " + request);
        } else {
            System.out.println("REJECT: " + request);
        }
    }

    private void dealForeign(Request request) {
        try {
            sleep(4000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if ((request.getBackTime() - request.getLeaveTime()) < 24 * 30) {
            System.out.println("ALLOW: " + request);
        } else {
            System.out.println("REJECT: " + request);
        }
    }
}

