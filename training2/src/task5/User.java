package task5;

public class User implements Observer {
    private String name;
    private String msg;

    public User(String name) {
        this.name = name;
    }

    public void update(String msg) {
        this.msg = msg;
        System.out.println(name + ": " + msg);
    } //在该方法中打印msg
}
