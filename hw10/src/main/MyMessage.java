package main;

import com.oocourse.spec2.main.Group;
import com.oocourse.spec2.main.Message;
import com.oocourse.spec2.main.Person;

public class MyMessage implements Message {
    private final int id;
    private final int socialValue;
    private final int type;
    private final Person person1;
    private final Person person2;
    private final Group group;

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Person messagePerson2) {
        id = messageId;
        socialValue = messageSocialValue;
        type = 0;
        person1 = messagePerson1;
        person2 = messagePerson2;
        group = null;
    }

    public MyMessage(int messageId, int messageSocialValue, Person messagePerson1,
                     Group messageGroup) {
        id = messageId;
        socialValue = messageSocialValue;
        type = 1;
        person1 = messagePerson1;
        person2 = null;
        group = messageGroup;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getSocialValue() {
        return socialValue;
    }

    public Person getPerson1() {
        return person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public Group getGroup() {
        return group;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Message)) {
            return false;
        } else {
            return ((Message) obj).getId() == id;
        }
    }
}
