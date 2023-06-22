import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UmlStateChartApi;
import com.oocourse.uml2.models.common.ElementType;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlTransition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UmlStateChart implements UmlStateChartApi {
    private final HashMap<String, UmlElement> elements = new HashMap<>();
    private final HashMap<String, ArrayList<String>> stateMachines = new HashMap<>();
    private final HashMap<String, String> regions = new HashMap<>();
    private final HashMap<String, HashMap<String, ArrayList<String>>> states = new HashMap<>();
    private final HashMap<String, ArrayList<String>> pseudoStates = new HashMap<>();
    private final HashMap<String, ArrayList<String>> finalStates = new HashMap<>();
    private final HashMap<String, HashMap<String, ArrayList<String>>> transitions = new HashMap<>();
    private final HashMap<String, ArrayList<String>> events = new HashMap<>();
    private final HashMap<String, ArrayList<String>> behaviors = new HashMap<>();

    public UmlStateChart(UmlElement... elements) {
        for (UmlElement element : elements) {
            this.elements.put(element.getId(), element);
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_STATE_MACHINE) {
                if (!stateMachines.containsKey(element.getName())) {
                    stateMachines.put(element.getName(), new ArrayList<>());
                }
                stateMachines.get(element.getName()).add(element.getId());
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_REGION) {
                regions.put(element.getParentId(), element.getId());
                states.put(element.getId(), new HashMap<>());
                pseudoStates.put(element.getId(), new ArrayList<>());
                finalStates.put(element.getId(), new ArrayList<>());
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_STATE) {
                if (!states.get(element.getParentId()).containsKey(element.getName())) {
                    states.get(element.getParentId()).put(element.getName(), new ArrayList<>());
                }
                states.get(element.getParentId()).get(element.getName()).add(element.getId());
                transitions.put(element.getId(), new HashMap<>());
            }
            if (element.getElementType() == ElementType.UML_PSEUDOSTATE) {
                pseudoStates.get(element.getParentId()).add(element.getId());
                transitions.put(element.getId(), new HashMap<>());
            }
            if (element.getElementType() == ElementType.UML_FINAL_STATE) {
                finalStates.get(element.getParentId()).add(element.getId());
                transitions.put(element.getId(), new HashMap<>());
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_TRANSITION) {
                if (!transitions.get(((UmlTransition) element).getSource()).
                        containsKey(((UmlTransition) element).getTarget())) {
                    transitions.get(((UmlTransition) element).getSource()).
                            put(((UmlTransition) element).getTarget(), new ArrayList<>());
                }
                transitions.get(((UmlTransition) element).getSource()).
                        get(((UmlTransition) element).getTarget()).add(element.getId());
                events.put(element.getId(), new ArrayList<>());
                behaviors.put(element.getId(), new ArrayList<>());
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_EVENT) {
                events.get(element.getParentId()).add(element.getId());
            }
            if (element.getElementType() == ElementType.UML_OPAQUE_BEHAVIOR) {
                behaviors.get(element.getParentId()).add(element.getId());
            }
        }
    }

    public int getStateCount(String stateMachineName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException {
        if (!stateMachines.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (stateMachines.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        String regionId = regions.get(stateMachines.get(stateMachineName).get(0));
        int count = 0;
        for (ArrayList<String> stateIds : states.get(regionId).values()) {
            count += stateIds.size();
        }
        count += pseudoStates.get(regionId).size();
        count += finalStates.get(regionId).size();
        return count;
    }

    public void dfs(String id, HashMap<String, Integer> vis) {
        vis.put(id, 1);
        for (String stateId : transitions.get(id).keySet()) {
            if (vis.get(stateId) == 0) {
                dfs(stateId, vis);
            }
        }
    }

    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        if (!stateMachines.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (stateMachines.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        String regionId = regions.get(stateMachines.get(stateMachineName).get(0));
        if (!states.get(regionId).containsKey(stateName)) {
            throw new StateNotFoundException(stateMachineName, stateName);
        }
        if (states.get(regionId).get(stateName).size() > 1) {
            throw new StateDuplicatedException(stateMachineName, stateName);
        }
        HashMap<String, Integer> vis = new HashMap<>();
        for (String stateId : pseudoStates.get(regionId)) {
            vis.put(stateId, 0);
        }
        for (String stateId : finalStates.get(regionId)) {
            vis.put(stateId, 0);
        }
        for (ArrayList<String> stateIds : states.get(regionId).values()) {
            for (String stateId : stateIds) {
                vis.put(stateId, 0);
            }
        }
        String pseudoStateId = pseudoStates.get(regionId).get(0);
        dfs(pseudoStateId, vis);
        int flag = 0;
        for (String stateId : finalStates.get(regionId)) {
            if (vis.get(stateId) == 1) {
                flag = 1;
            }
        }
        if (flag == 0) {
            return false;
        }
        vis = new HashMap<>();
        for (String stateId : pseudoStates.get(regionId)) {
            vis.put(stateId, 0);
        }
        for (String stateId : finalStates.get(regionId)) {
            vis.put(stateId, 0);
        }
        for (ArrayList<String> stateIds : states.get(regionId).values()) {
            for (String stateId : stateIds) {
                vis.put(stateId, 0);
            }
        }
        String id = states.get(regionId).get(stateName).get(0);
        vis.put(id, 1);
        dfs(pseudoStateId, vis);
        for (String stateId : finalStates.get(regionId)) {
            if (vis.get(stateId) == 1) {
                return false;
            }
        }
        return true;
    }

    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName
    )
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException,
            TransitionNotFoundException {
        if (!stateMachines.containsKey(stateMachineName)) {
            throw new StateMachineNotFoundException(stateMachineName);
        }
        if (stateMachines.get(stateMachineName).size() > 1) {
            throw new StateMachineDuplicatedException(stateMachineName);
        }
        String regionId = regions.get(stateMachines.get(stateMachineName).get(0));
        if (!states.get(regionId).containsKey(sourceStateName)) {
            throw new StateNotFoundException(stateMachineName, sourceStateName);
        }
        if (states.get(regionId).get(sourceStateName).size() > 1) {
            throw new StateDuplicatedException(stateMachineName, sourceStateName);
        }
        if (!states.get(regionId).containsKey(targetStateName)) {
            throw new StateNotFoundException(stateMachineName, targetStateName);
        }
        if (states.get(regionId).get(targetStateName).size() > 1) {
            throw new StateDuplicatedException(stateMachineName, targetStateName);
        }
        String stateId1 = states.get(regionId).get(sourceStateName).get(0);
        String stateId2 = states.get(regionId).get(targetStateName).get(0);
        if (!transitions.get(stateId1).containsKey(stateId2)) {
            throw new TransitionNotFoundException(stateMachineName,
                    sourceStateName, targetStateName);
        }
        ArrayList<String> triggers = new ArrayList<>();
        for (String transitionId : transitions.get(stateId1).get(stateId2)) {
            for (String eventId : events.get(transitionId)) {
                triggers.add(elements.get(eventId).getName());
            }
        }
        return triggers;
    }
}
