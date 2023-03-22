package tray;

import com.oocourse.elevator3.PersonRequest;
import com.oocourse.elevator3.Request;

public class CustomRequest extends Request {
    private final int personId;
    private char fromBuilding;
    private int fromFloor;
    private char toBuilding;
    private int toFloor;
    private final char finalBuilding;
    private final int finalFloor;

    public CustomRequest(PersonRequest personRequest) {
        personId = personRequest.getPersonId();
        fromBuilding = personRequest.getFromBuilding();
        fromFloor = personRequest.getFromFloor();
        finalBuilding = personRequest.getToBuilding();
        finalFloor = personRequest.getToFloor();
    }

    public int getPersonId() {
        return personId;
    }

    public char getFromBuilding() {
        return fromBuilding;
    }

    public int getFromFloor() {
        return fromFloor;
    }

    public char getToBuilding() {
        return toBuilding;
    }

    public int getToFloor() {
        return toFloor;
    }

    public char getFinalBuilding() {
        return finalBuilding;
    }

    public int getFinalFloor() {
        return finalFloor;
    }

    public void setFromBuilding(char fromBuilding) {
        this.fromBuilding = fromBuilding;
    }

    public void setFromFloor(int fromFloor) {
        this.fromFloor = fromFloor;
    }

    public void setToBuilding(char toBuilding) {
        this.toBuilding = toBuilding;
    }

    public void setToFloor(int toFloor) {
        this.toFloor = toFloor;
    }

    public int getRequestDirection() {
        if (toFloor == fromFloor) {
            return 1;
        } else {
            return (toFloor - fromFloor) / Math.abs(toFloor - fromFloor);
        }
    }
}
