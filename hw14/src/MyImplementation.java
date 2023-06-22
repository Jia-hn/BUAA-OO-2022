import com.oocourse.uml2.interact.common.Pair;
import com.oocourse.uml2.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml2.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml2.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml2.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml2.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml2.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml2.interact.format.UserApi;
import com.oocourse.uml2.models.common.Visibility;
import com.oocourse.uml2.models.elements.UmlElement;
import com.oocourse.uml2.models.elements.UmlLifeline;

import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final UmlClassModel umlClassModel;
    private final UmlCollaboration umlCollaboration;
    private final UmlStateChart umlStateChart;

    public MyImplementation(UmlElement... elements) {
        umlClassModel = new UmlClassModel(elements);
        umlCollaboration = new UmlCollaboration(elements);
        umlStateChart = new UmlStateChart(elements);
    }

    @Override
    public int getClassCount() {
        return umlClassModel.getClassCount();
    }

    @Override
    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModel.getClassSubClassCount(className);
    }

    @Override
    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModel.getClassOperationCount(className);
    }

    @Override
    public Map<Visibility, Integer> getClassOperationVisibility(
            String className, String methodName) throws
            ClassNotFoundException, ClassDuplicatedException {
        return umlClassModel.getClassOperationVisibility(className, methodName);
    }

    @Override
    public List<Integer> getClassOperationCouplingDegree(
            String className, String methodName) throws ClassNotFoundException,
            ClassDuplicatedException, MethodWrongTypeException, MethodDuplicatedException {
        return umlClassModel.getClassOperationCouplingDegree(className, methodName);
    }

    @Override
    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModel.getClassAttributeCouplingDegree(className);
    }

    @Override
    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        return umlClassModel.getClassImplementInterfaceList(className);
    }

    @Override
    public int getClassDepthOfInheritance(String className) throws
            ClassNotFoundException, ClassDuplicatedException {
        return umlClassModel.getClassDepthOfInheritance(className);
    }

    @Override
    public int getParticipantCount(String interactionName)
            throws InteractionNotFoundException, InteractionDuplicatedException {
        return umlCollaboration.getParticipantCount(interactionName);
    }

    @Override
    public UmlLifeline getParticipantCreator(String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException
            , LifelineDuplicatedException,
            LifelineNeverCreatedException, LifelineCreatedRepeatedlyException {
        return umlCollaboration.getParticipantCreator(interactionName, lifelineName);
    }

    @Override
    public Pair<Integer, Integer> getParticipantLostAndFound(
            String interactionName, String lifelineName)
            throws InteractionNotFoundException, InteractionDuplicatedException,
            LifelineNotFoundException, LifelineDuplicatedException {
        return umlCollaboration.getParticipantLostAndFound(interactionName, lifelineName);
    }

    @Override
    public int getStateCount(String stateMachineName) throws
            StateMachineNotFoundException, StateMachineDuplicatedException {
        return umlStateChart.getStateCount(stateMachineName);
    }

    @Override
    public boolean getStateIsCriticalPoint(String stateMachineName, String stateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException {
        return umlStateChart.getStateIsCriticalPoint(stateMachineName, stateName);
    }

    @Override
    public List<String> getTransitionTrigger(
            String stateMachineName, String sourceStateName, String targetStateName)
            throws StateMachineNotFoundException, StateMachineDuplicatedException,
            StateNotFoundException, StateDuplicatedException, TransitionNotFoundException {
        return umlStateChart.getTransitionTrigger(
                stateMachineName, sourceStateName, targetStateName);
    }
}