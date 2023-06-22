package main;

import com.oocourse.spec1.main.Person;

import java.util.HashMap;

public class MyPerson implements Person {
    private final int id;
    private final String name;
    private final int age;
    private final HashMap<Integer, Integer> values;

    public MyPerson(int id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
        values = new HashMap<>();
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
}
