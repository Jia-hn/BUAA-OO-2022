package main;

import com.oocourse.spec3.exceptions.EmojiIdNotFoundException;
import com.oocourse.spec3.exceptions.EqualEmojiIdException;
import com.oocourse.spec3.exceptions.EqualGroupIdException;
import com.oocourse.spec3.exceptions.EqualMessageIdException;
import com.oocourse.spec3.exceptions.EqualPersonIdException;
import com.oocourse.spec3.exceptions.EqualRelationException;
import com.oocourse.spec3.exceptions.GroupIdNotFoundException;
import com.oocourse.spec3.exceptions.MessageIdNotFoundException;
import com.oocourse.spec3.exceptions.PersonIdNotFoundException;
import com.oocourse.spec3.exceptions.RelationNotFoundException;
import com.oocourse.spec3.main.EmojiMessage;
import com.oocourse.spec3.main.Group;
import com.oocourse.spec3.main.Message;
import com.oocourse.spec3.main.Network;
import com.oocourse.spec3.main.NoticeMessage;
import com.oocourse.spec3.main.Person;
import com.oocourse.spec3.main.RedEnvelopeMessage;
import exceptions.MyEmojiIdNotFoundException;
import exceptions.MyEqualEmojiIdException;
import exceptions.MyEqualGroupIdException;
import exceptions.MyEqualMessageIdException;
import exceptions.MyEqualPersonIdException;
import exceptions.MyEqualRelationException;
import exceptions.MyGroupIdNotFoundException;
import exceptions.MyMessageIdNotFoundException;
import exceptions.MyPersonIdNotFoundException;
import exceptions.MyRelationNotFoundException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class MyNetwork implements Network {
    private final HashMap<Integer, Person> people;
    private final HashMap<Integer, Group> groups;
    private final HashMap<Integer, Message> messages;
    private final HashMap<Integer, Integer> emoji;
    private final UnionFindSet unionFindSet;

    public MyNetwork() {
        people = new HashMap<>();
        groups = new HashMap<>();
        messages = new HashMap<>();
        emoji = new HashMap<>();
        unionFindSet = new UnionFindSet();
    }

    public boolean contains(int id) {
        return people.containsKey(id);
    }

    public Person getPerson(int id) {
        return people.get(id);
    }

    public void addPerson(Person person) throws EqualPersonIdException {
        if (contains(person.getId())) {
            throw new MyEqualPersonIdException(person.getId());
        }
        people.put(person.getId(), person);
        unionFindSet.put(person.getId());
    }

    public void addRelation(int id1, int id2, int value) throws
            PersonIdNotFoundException, EqualRelationException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyEqualRelationException(id1, id2);
        }
        ((MyPerson) getPerson(id1)).link(getPerson(id2), value);
        ((MyPerson) getPerson(id2)).link(getPerson(id1), value);
        unionFindSet.union(id1, id2);
    }

    public int queryValue(int id1, int id2) throws
            PersonIdNotFoundException, RelationNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        if (!getPerson(id1).isLinked(getPerson(id2))) {
            throw new MyRelationNotFoundException(id1, id2);
        }
        return getPerson(id1).queryValue(getPerson(id2));
    }

    public int queryPeopleSum() {
        return people.size();
    }

    public boolean isCircle(int id1, int id2) throws PersonIdNotFoundException {
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!contains(id2)) {
            throw new MyPersonIdNotFoundException(id2);
        }
        return unionFindSet.find(id1) == unionFindSet.find(id2);
    }

    public int queryBlockSum() {
        int sum = 0;
        for (Integer id : unionFindSet.getSet().keySet()) {
            if (unionFindSet.getSet().get(id).equals(id)) {
                sum++;
            }
        }
        return sum;
    }

    public int queryLeastConnection(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        ArrayList<Relation> relations = new ArrayList<>();
        UnionFindSet temp = new UnionFindSet();
        for (Integer id1 : people.keySet()) {
            if (isCircle(id, id1)) {
                temp.put(id1);
                for (Integer id2 : ((MyPerson) getPerson(id1)).getValues().keySet()) {
                    relations.add(new Relation(id1, id2, ((MyPerson)
                            getPerson(id1)).getValues().get(id2)));
                }
            }
        }
        Collections.sort(relations);
        int sum = 0;
        for (Relation relation : relations) {
            if (temp.find(relation.getId1()) != temp.find(relation.getId2())) {
                temp.union(relation.getId1(), relation.getId2());
                sum += relation.getValue();
            }
        }
        return sum;
    }

    public boolean containsGroup(int id) {
        return groups.containsKey(id);
    }

    public Group getGroup(int id) {
        return groups.get(id);
    }

    public void addGroup(Group group) throws EqualGroupIdException {
        if (containsGroup(group.getId())) {
            throw new MyEqualGroupIdException(group.getId());
        }
        groups.put(group.getId(), group);
    }

    public void addToGroup(int id1, int id2) throws GroupIdNotFoundException,
            PersonIdNotFoundException, EqualPersonIdException {
        if (!containsGroup(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        if (getGroup(id2).getSize() < 1111) {
            getGroup(id2).addPerson(getPerson(id1));
        }
    }

    public int queryGroupPeopleSum(int id) throws GroupIdNotFoundException {
        if (!containsGroup(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getSize();
    }

    public int queryGroupValueSum(int id) throws GroupIdNotFoundException {
        if (!containsGroup(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getValueSum();
    }

    public int queryGroupAgeVar(int id) throws GroupIdNotFoundException {
        if (!containsGroup(id)) {
            throw new MyGroupIdNotFoundException(id);
        }
        return getGroup(id).getAgeVar();
    }

    public void delFromGroup(int id1, int id2)
            throws GroupIdNotFoundException, PersonIdNotFoundException, EqualPersonIdException {
        if (!containsGroup(id2)) {
            throw new MyGroupIdNotFoundException(id2);
        }
        if (!contains(id1)) {
            throw new MyPersonIdNotFoundException(id1);
        }
        if (!getGroup(id2).hasPerson(getPerson(id1))) {
            throw new MyEqualPersonIdException(id1);
        }
        getGroup(id2).delPerson(getPerson(id1));
    }

    public boolean containsMessage(int id) {
        return messages.containsKey(id);
    }

    public Message getMessage(int id) {
        return messages.get(id);
    }

    public void addMessage(Message message) throws
            EqualMessageIdException, EmojiIdNotFoundException, EqualPersonIdException {
        if (containsMessage(message.getId())) {
            throw new MyEqualMessageIdException(message.getId());
        }
        if ((message instanceof EmojiMessage) &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId())) {
            throw new MyEmojiIdNotFoundException(((EmojiMessage) message).getEmojiId());
        }
        if (message.getType() == 0 && message.getPerson1() == message.getPerson2()) {
            throw new MyEqualPersonIdException(message.getPerson1().getId());
        }
        messages.put(message.getId(), message);
    }

    public void sendMessage(int id) throws
            RelationNotFoundException, MessageIdNotFoundException, PersonIdNotFoundException {
        Message message = getMessage(id);
        if (!containsMessage(id)) {
            throw new MyMessageIdNotFoundException(id);
        }
        if (message.getType() == 0 && !(message.getPerson1().isLinked(message.getPerson2()))) {
            throw new MyRelationNotFoundException(message.getPerson1().getId(),
                    message.getPerson2().getId());
        }
        if (message.getType() == 1 && !(message.getGroup().hasPerson(message.getPerson1()))) {
            throw new MyPersonIdNotFoundException(message.getPerson1().getId());
        }
        if (message.getType() == 0) {
            messages.remove(id);
            message.getPerson1().addSocialValue(message.getSocialValue());
            message.getPerson2().addSocialValue(message.getSocialValue());
            if (message instanceof RedEnvelopeMessage) {
                message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
                message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
            }
            if (message instanceof EmojiMessage) {
                emoji.put(((EmojiMessage) message).getEmojiId(),
                        emoji.get(((EmojiMessage) message).getEmojiId()) + 1);
            }
            ((MyPerson) message.getPerson2()).addMessage(message);
        }
        if (message.getType() == 1) {
            messages.remove(id);
            for (Person person : ((MyGroup) message.getGroup()).getPeople().values()) {
                person.addSocialValue(message.getSocialValue());
            }
            if (message instanceof RedEnvelopeMessage) {
                int size = message.getGroup().getSize();
                int money = ((RedEnvelopeMessage) message).getMoney() / size;
                for (Person person : ((MyGroup) message.getGroup()).getPeople().values()) {
                    if (person.equals(message.getPerson1())) {
                        person.addMoney(-money * (size - 1));
                    } else {
                        person.addMoney(money);
                    }
                }
            }
            if (message instanceof EmojiMessage) {
                emoji.put(((EmojiMessage) message).getEmojiId(),
                        emoji.get(((EmojiMessage) message).getEmojiId()) + 1);
            }
        }
    }

    public int querySocialValue(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getSocialValue();
    }

    public List<Message> queryReceivedMessages(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getReceivedMessages();
    }

    public boolean containsEmojiId(int id) {
        return emoji.containsKey(id);
    }

    public void storeEmojiId(int id) throws EqualEmojiIdException {
        if (containsEmojiId(id)) {
            throw new MyEqualEmojiIdException(id);
        }
        emoji.put(id, 0);
    }

    public int queryMoney(int id) throws PersonIdNotFoundException {
        if (!contains(id)) {
            throw new MyPersonIdNotFoundException(id);
        }
        return getPerson(id).getMoney();
    }

    public int queryPopularity(int id) throws EmojiIdNotFoundException {
        if (!containsEmojiId(id)) {
            throw new MyEmojiIdNotFoundException(id);
        }
        return emoji.get(id);
    }

    public int deleteColdEmoji(int limit) {
        emoji.values().removeIf(value -> value < limit);
        messages.values().removeIf(message -> (message instanceof EmojiMessage) &&
                !containsEmojiId(((EmojiMessage) message).getEmojiId()));
        return emoji.size();
    }

    public void clearNotices(int personId) throws PersonIdNotFoundException {
        if (!contains(personId)) {
            throw new MyPersonIdNotFoundException(personId);
        }
        LinkedList<Message> messages = (LinkedList<Message>) getPerson(personId).getMessages();
        messages.removeIf(message -> message instanceof NoticeMessage);
    }

    public int sendIndirectMessage(int id) throws
            MessageIdNotFoundException {
        if (!containsMessage(id) || (containsMessage(id) && getMessage(id).getType() == 1)) {
            throw new MyMessageIdNotFoundException(id);
        }
        Message message = getMessage(id);
        if (unionFindSet.find(message.getPerson1().getId())
                != unionFindSet.find(message.getPerson2().getId())) {
            return -1;
        }
        messages.remove(id);
        message.getPerson1().addSocialValue(message.getSocialValue());
        message.getPerson2().addSocialValue(message.getSocialValue());
        if (message instanceof RedEnvelopeMessage) {
            message.getPerson1().addMoney(-((RedEnvelopeMessage) message).getMoney());
            message.getPerson2().addMoney(((RedEnvelopeMessage) message).getMoney());
        }
        if (message instanceof EmojiMessage) {
            emoji.put(((EmojiMessage) message).getEmojiId(),
                    emoji.get(((EmojiMessage) message).getEmojiId()) + 1);
        }
        ((MyPerson) message.getPerson2()).addMessage(message);

        HashMap<Integer, Integer> vis = new HashMap<>();
        HashMap<Integer, Integer> dis = new HashMap<>();
        for (Integer integer : people.keySet()) {
            vis.put(integer, 0);
            dis.put(integer, 0x7fffffff);
        }
        PriorityQueue<Relation> heap = new PriorityQueue<>();
        dis.put(message.getPerson1().getId(), 0);
        heap.add(new Relation(0, message.getPerson1().getId(), 0));
        for (int i = 0; i < people.size(); i++) {
            while (!heap.isEmpty() && vis.get(heap.element().getId2()) != 0) {
                heap.remove();
            }
            if (heap.isEmpty()) {
                break;
            }
            Relation relation = heap.remove();
            int index = relation.getId2();
            vis.put(index, 1);
            dis.put(index, relation.getValue());
            if (index == message.getPerson2().getId()) {
                break;
            }
            for (Integer integer : ((MyPerson) getPerson(index)).getValues().keySet()) {
                if (vis.get(integer) == 0) {
                    heap.add(new Relation(index, integer, ((MyPerson) getPerson(index)).
                            getValues().get(integer) + dis.get(index)));
                }
            }
        }
        return dis.get(message.getPerson2().getId());
    }
}
