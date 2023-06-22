import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.format.UmlCollaborationApi;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.common.MessageSort;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlLifeline;
import com.oocourse.uml2.models.elements.UmlMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class UmlCollaboration implements UmlCollaborationApi {
    private final HashMap<String, UmlElement> elements = new HashMap<>();
    private final HashMap<String, ArrayList<String>> interactions = new HashMap<>();
    private final HashMap<String, HashMap<String, ArrayList<String>>> lifelines = new HashMap<>();
    private final HashMap<String, HashSet<String>> endPoints = new HashMap<>();
    private final HashMap<String, ArrayList<String>> receivedMessages = new HashMap<>();
    private final HashMap<String, ArrayList<String>> sentMessages = new HashMap<>();

    public UmlCollaboration(UmlElement... elements) {
        for (UmlElement element : elements) {
            this.elements.put(element.getId(), element);
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_INTERACTION) {
                if (!interactions.containsKey(element.getName())) {
                    interactions.put(element.getName(), new ArrayList<>());
                }
                interactions.get(element.getName()).add(element.getId());
                lifelines.put(element.getId(), new HashMap<>());
                endPoints.put(element.getId(), new HashSet<>());
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_LIFELINE) {
                if (!lifelines.get(element.getParentId()).containsKey(element.getName())) {
                    lifelines.get(element.getParentId()).put(element.getName(), new ArrayList<>());
                }
                lifelines.get(element.getParentId()).get(element.getName()).add(element.getId());
                receivedMessages.put(element.getId(), new ArrayList<>());
                sentMessages.put(element.getId(), new ArrayList<>());
            } else if (element.getElementType() == ElementType.UML_ENDPOINT) {
                endPoints.get(element.getParentId()).add(element.getId());
                receivedMessages.put(element.getId(), new ArrayList<>());
                sentMessages.put(element.getId(), new ArrayList<>());
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_MESSAGE) {
                String id1 = ((UmlMessage) element).getSource();
                String id2 = ((UmlMessage) element).getTarget();
                sentMessages.get(id1).add(element.getId());
                receivedMessages.get(id2).add(element.getId());
            }
        }
    }

    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        if (!interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        String interactionId = interactions.get(interactionName).get(0);
        int count = 0;
        for (ArrayList<String> lifelineIds : lifelines.get(interactionId).values()) {
            count += lifelineIds.size();
        }
        return count;
    }

    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        if (!interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        String interactionId = interactions.get(interactionName).get(0);
        if (!lifelines.get(interactionId).containsKey(lifelineName)) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        if (lifelines.get(interactionId).get(lifelineName).size() > 1) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        String lifelineId = lifelines.get(interactionId).get(lifelineName).get(0);
        int count = 0;
        UmlLifeline umlLifeline = null;
        for (String messageId : receivedMessages.get(lifelineId)) {
            if (((UmlMessage) elements.get(messageId)).getMessageSort()
                    == MessageSort.CREATE_MESSAGE) {
                count++;
                umlLifeline = (UmlLifeline) elements.get(
                        ((UmlMessage) elements.get(messageId)).getSource());
            }
        }
        if (count == 0) {
            throw new LifelineNeverCreatedException(interactionName, lifelineName);
        }
        if (count > 1) {
            throw new LifelineCreatedRepeatedlyException(interactionName, lifelineName);
        }
        return umlLifeline;
    }

    public Pair<Integer, Integer> getParticipantLostAndFound(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        if (!interactions.containsKey(interactionName)) {
            throw new InteractionNotFoundException(interactionName);
        }
        if (interactions.get(interactionName).size() > 1) {
            throw new InteractionDuplicatedException(interactionName);
        }
        String interactionId = interactions.get(interactionName).get(0);
        if (!lifelines.get(interactionId).containsKey(lifelineName)) {
            throw new LifelineNotFoundException(interactionName, lifelineName);
        }
        if (lifelines.get(interactionId).get(lifelineName).size() > 1) {
            throw new LifelineDuplicatedException(interactionName, lifelineName);
        }
        String lifelineId = lifelines.get(interactionId).get(lifelineName).get(0);
        Integer count1 = 0;
        Integer count2 = 0;
        for (String messageId : receivedMessages.get(lifelineId)) {
            if (endPoints.get(interactionId).contains(
                    ((UmlMessage) elements.get(messageId)).getSource())) {
                count1++;
            }
        }
        for (String messageId : sentMessages.get(lifelineId)) {
            if (endPoints.get(interactionId).contains(
                    ((UmlMessage) elements.get(messageId)).getTarget())) {
                count2++;
            }
        }
        return new Pair<>(count1, count2);
    }
}
