package main;

import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Person;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Integer> values;
    private int money;
    private int socialValue;
    private final LinkedList<Message> messages;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        values = new HashMap<>();
        money = 0;
        socialValue = 0;
        messages = new LinkedList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public HashMap<Integer, Integer> getValues() {
        return values;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Person)) {
            return false;
        } else {
            return ((Person) obj).getId() == id;
        }
    }

    public void link(Person person, int value) {
        values.put(person.getId(), value);
    }

    public boolean isLinked(Person person) {
        return (person.getId() == id) || values.containsKey(person.getId());
    }

    public int queryValue(Person person) {
        if (values.containsKey(person.getId())) {
            return values.get(person.getId());
        }
        return 0;
    }

    public int compareTo(Person p2) {
        return name.compareTo(p2.getName());
    }

    public void addSocialValue(int num) {
        socialValue += num;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public void addMessage(Message message) {
        messages.addFirst(message);
    }

    public List<Message> getMessages() {
        return messages;
    }

    public List<Message> getReceivedMessages() {
        LinkedList<Message> receivedMessages = new LinkedList<>();
        if (messages.size() < 4) {
            receivedMessages = (LinkedList<Message>) messages.clone();
        } else {
            receivedMessages.add(messages.get(0));
            receivedMessages.add(messages.get(1));
            receivedMessages.add(messages.get(2));
            receivedMessages.add(messages.get(3));
        }
        return receivedMessages;
    }

    public void addMoney(int num) {
        money += num;
    }

    public int getMoney() {
        return money;
    }
}
