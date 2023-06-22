import com.oocourse.uml3.interact.common.Pair;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.InteractionDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.InteractionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.LifelineCreatedRepeatedlyException;
import com.oocourse.uml3.interact.exceptions.user.LifelineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNeverCreatedException;
import com.oocourse.uml3.interact.exceptions.user.LifelineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.PreCheckRuleException;
import com.oocourse.uml3.interact.exceptions.user.StateDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.StateMachineNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.StateNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.TransitionNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule006Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule007Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule008Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule009Exception;
import com.oocourse.uml3.interact.format.UserApi;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlLifeline;

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

    @Override
    public void checkForAllRules() throws PreCheckRuleException {
        checkForUml001();
        checkForUml002();
        checkForUml003();
        checkForUml004();
        checkForUml005();
        checkForUml006();
        checkForUml007();
        checkForUml008();
        checkForUml009();
    }

    @Override
    public void checkForUml001() throws UmlRule001Exception {
        umlClassModel.checkForUml001();
    }

    @Override
    public void checkForUml002() throws UmlRule002Exception {
        umlClassModel.checkForUml002();
    }

    @Override
    public void checkForUml003() throws UmlRule003Exception {
        umlClassModel.checkForUml003();
    }

    @Override
    public void checkForUml004() throws UmlRule004Exception {
        umlClassModel.checkForUml004();
    }

    @Override
    public void checkForUml005() throws UmlRule005Exception {
        umlClassModel.checkForUml005();
    }

    @Override
    public void checkForUml006() throws UmlRule006Exception {
        umlCollaboration.checkForUml006();
    }

    @Override
    public void checkForUml007() throws UmlRule007Exception {
        umlCollaboration.checkForUml007();
    }

    @Override
    public void checkForUml008() throws UmlRule008Exception {
        umlStateChart.checkForUml008();
    }

    @Override
    public void checkForUml009() throws UmlRule009Exception {
        umlStateChart.checkForUml009();
    }
}