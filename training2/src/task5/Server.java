package task5;

import java.util.ArrayList;

public class Server implements Observerable {
    private ArrayList<Observer> observers;
    private String msg;

    public Server() {
        observers = new ArrayList<>();
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void removeObserver(Observer observer) {
        observers.remove(observer);
    }

    public void notifyObserver(String msg) {
        this.msg = msg;
        System.out.println("server: " + msg);
        for (Observer observer : observers) {
            observer.update(msg);
        }
    } //打印msg并调用每个注册过的观察者的update方法
}
