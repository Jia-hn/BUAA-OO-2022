import com.oocourse.uml1.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml1.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml1.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml1.interact.format.UserApi;
import com.oocourse.uml1.models.common.Direction;
import com.oocourse.uml1.models.common.ElementType;
import com.oocourse.uml1.models.common.NameableType;
import com.oocourse.uml1.models.common.NamedType;
import com.oocourse.uml1.models.common.ReferenceType;
import com.oocourse.uml1.models.common.Visibility;
import com.oocourse.uml1.models.elements.UmlAttribute;
import com.oocourse.uml1.models.elements.UmlElement;
import com.oocourse.uml1.models.elements.UmlGeneralization;
import com.oocourse.uml1.models.elements.UmlInterfaceRealization;
import com.oocourse.uml1.models.elements.UmlOperation;
import com.oocourse.uml1.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class MyImplementation implements UserApi {
    private final HashMap<String, UmlElement> elements = new HashMap<>();
    private final HashMap<String, ArrayList<String>> classes = new HashMap<>();
    private final HashMap<String, ArrayList<String>> interfaces = new HashMap<>();
    private final HashMap<String, ArrayList<String>> attributes = new HashMap<>();
    private final HashMap<String, HashMap<String, ArrayList<String>>> operations = new HashMap<>();
    private final HashMap<String, ArrayList<String>> parameters = new HashMap<>();
    private final HashMap<String, ArrayList<String>> generalize = new HashMap<>();
    private final HashMap<String, ArrayList<String>> generalized = new HashMap<>();
    private final HashMap<String, ArrayList<String>> realize = new HashMap<>();
    private final HashMap<String, ArrayList<String>> realized = new HashMap<>();

    public MyImplementation(UmlElement... elements) {
        for (UmlElement element : elements) {
            this.elements.put(element.getId(), element);
            if (element.getElementType() == ElementType.UML_CLASS ||
                    element.getElementType() == ElementType.UML_INTERFACE) {
                attributes.put(element.getId(), new ArrayList<>());
                operations.put(element.getId(), new HashMap<>());
                generalize.put(element.getId(), new ArrayList<>());
                generalized.put(element.getId(), new ArrayList<>());
                if (element.getElementType() == ElementType.UML_CLASS) {
                    if (!classes.containsKey(element.getName())) {
                        classes.put(element.getName(), new ArrayList<>());
                    }
                    classes.get(element.getName()).add(element.getId());
                    realize.put(element.getId(), new ArrayList<>());
                } else {
                    if (!interfaces.containsKey(element.getName())) {
                        interfaces.put(element.getName(), new ArrayList<>());
                    }
                    interfaces.get(element.getName()).add(element.getId());
                    realized.put(element.getId(), new ArrayList<>());
                }
            }
        }
        for (UmlElement element : elements) {
            switch (element.getElementType()) {
                case UML_ATTRIBUTE:
                    attributes.get(element.getParentId()).add(element.getId());
                    break;
                case UML_OPERATION:
                    if (!operations.get(element.getParentId()).containsKey(element.getName())) {
                        operations.get(element.getParentId()).
                                put(element.getName(), new ArrayList<>());
                    }
                    operations.get(element.getParentId()).
                            get(element.getName()).add(element.getId());
                    parameters.put(element.getId(), new ArrayList<>());
                    break;
                case UML_GENERALIZATION:
                    generalize.get(((UmlGeneralization) element).getSource()).
                            add(((UmlGeneralization) element).getTarget());
                    generalized.get(((UmlGeneralization) element).getTarget()).
                            add(((UmlGeneralization) element).getSource());
                    break;
                case UML_INTERFACE_REALIZATION:
                    realize.get(((UmlInterfaceRealization) element).getSource()).
                            add(((UmlInterfaceRealization) element).getTarget());
                    realized.get(((UmlInterfaceRealization) element).getTarget()).
                            add(((UmlInterfaceRealization) element).getSource());
                    break;
                default:
                    break;
            }
        }
        for (UmlElement element : elements) {
            if (element.getElementType() == ElementType.UML_PARAMETER) {
                parameters.get(element.getParentId()).add(element.getId());
            }
        }
    }

    public int getClassCount() {
        int count = 0;
        for (ArrayList<String> classIds : classes.values()) {
            count += classIds.size();
        }
        return count;
    }

    public int getClassSubClassCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        return generalized.get(classes.get(className).get(0)).size();
    }

    public int getClassOperationCount(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        int count = 0;
        for (ArrayList<String> operationIds : operations.
                get(classes.get(className).get(0)).values()) {
            count += operationIds.size();
        }
        return count;
    }

    public Map<Visibility, Integer> getClassOperationVisibility(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        HashMap<Visibility, Integer> counts = new HashMap<>();
        for (Visibility visibility : Visibility.values()) {
            counts.put(visibility, 0);
        }
        if (!operations.get(classes.get(className).get(0)).containsKey(methodName)) {
            return counts;
        }
        for (String operationId : operations.get(classes.get(className).get(0)).get(methodName)) {
            Visibility visibility = ((UmlOperation) elements.get(operationId)).getVisibility();
            counts.put(visibility, counts.get(visibility) + 1);
        }
        return counts;
    }

    public boolean isNamedType1(NameableType type) {
        String[] names = {"byte", "short", "int", "long", "float",
            "double", "char", "boolean", "String"};
        if (type instanceof ReferenceType) {
            return true;
        }
        for (String name : names) {
            if (((NamedType) type).getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNamedType2(NameableType type) {
        return isNamedType1(type) || ((NamedType) type).getName().equals("void");
    }

    public boolean isSame(String operationId1, String operationId2) {
        HashMap<NameableType, Integer> counts = new HashMap<>();
        for (String parameterId : parameters.get(operationId1)) {
            UmlParameter parameter = (UmlParameter) elements.get(parameterId);
            if (parameter.getDirection().equals(Direction.RETURN)) {
                continue;
            }
            if (!counts.containsKey(parameter.getType())) {
                counts.put(parameter.getType(), 1);
            } else {
                counts.put(parameter.getType(), counts.get(parameter.getType()) + 1);
            }
        }
        for (String parameterId : parameters.get(operationId2)) {
            UmlParameter parameter = (UmlParameter) elements.get(parameterId);
            if (parameter.getDirection().equals(Direction.RETURN)) {
                continue;
            }
            if (!counts.containsKey(parameter.getType())) {
                counts.put(parameter.getType(), -1);
            } else {
                counts.put(parameter.getType(), counts.get(parameter.getType()) - 1);
            }
        }
        for (Integer count : counts.values()) {
            if (count != 0) {
                return false;
            }
        }
        return true;
    }

    public List<Integer> getClassOperationCouplingDegree(String className, String methodName)
            throws ClassNotFoundException, ClassDuplicatedException,
            MethodWrongTypeException, MethodDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        String classId = classes.get(className).get(0);
        ArrayList<Integer> couplingDegrees = new ArrayList<>();
        if (!operations.get(classId).containsKey(methodName)) {
            return couplingDegrees;
        }
        for (String operationId : operations.get(classId).get(methodName)) {
            for (String parameterId : parameters.get(operationId)) {
                UmlParameter parameter = (UmlParameter) elements.get(parameterId);
                if (parameter.getDirection().equals(Direction.IN) &&
                        !isNamedType1(parameter.getType())) {
                    throw new MethodWrongTypeException(className, methodName);
                } else if (parameter.getDirection().equals(Direction.RETURN) &&
                        !isNamedType2(parameter.getType())) {
                    throw new MethodWrongTypeException(className, methodName);
                }
            }
        }
        for (String operationId1 : operations.get(classId).get(methodName)) {
            for (String operationId2 : operations.get(classId).get(methodName)) {
                if (operationId1.equals(operationId2)) {
                    continue;
                }
                if (isSame(operationId1, operationId2)) {
                    throw new MethodDuplicatedException(className, methodName);
                }
            }
        }
        for (String operationId : operations.get(classId).get(methodName)) {
            HashSet<ReferenceType> referenceTypes = new HashSet<>();
            for (String parameterId: parameters.get(operationId)) {
                UmlParameter parameter = (UmlParameter) elements.get(parameterId);
                if (parameter.getType() instanceof ReferenceType &&
                        !((ReferenceType) parameter.getType()).getReferenceId().equals(classId)) {
                    referenceTypes.add((ReferenceType) parameter.getType());
                }
            }
            couplingDegrees.add(referenceTypes.size());
        }
        return couplingDegrees;
    }

    public int getClassAttributeCouplingDegree(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        String id = classes.get(className).get(0);
        HashSet<ReferenceType> referenceTypes = new HashSet<>();
        String classId = classes.get(className).get(0);
        while (classId != null) {
            for (String attributeId : attributes.get(classId)) {
                UmlAttribute attribute = (UmlAttribute) elements.get(attributeId);
                if (attribute.getType() instanceof ReferenceType &&
                        !((ReferenceType) attribute.getType()).getReferenceId().equals(id)) {
                    referenceTypes.add((ReferenceType) attribute.getType());
                }
            }
            if (generalize.get(classId).size() != 0) {
                classId = generalize.get(classId).get(0);
            } else {
                classId = null;
            }
        }
        return referenceTypes.size();
    }

    public void dfs(String id, HashSet<String> interfaceIds) {
        interfaceIds.add(elements.get(id).getName());
        for (String interfaceId : generalize.get(id)) {
            dfs(interfaceId, interfaceIds);
        }
    }

    public List<String> getClassImplementInterfaceList(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        HashSet<String> interfaceIds = new HashSet<>();
        String classId = classes.get(className).get(0);
        while (classId != null) {
            for (String interfaceId : realize.get(classId)) {
                dfs(interfaceId, interfaceIds);
            }
            if (generalize.get(classId).size() != 0) {
                classId = generalize.get(classId).get(0);
            } else {
                classId = null;
            }
        }
        return new ArrayList<>(interfaceIds);
    }

    public int getClassDepthOfInheritance(String className)
            throws ClassNotFoundException, ClassDuplicatedException {
        if (!classes.containsKey(className)) {
            throw new ClassNotFoundException(className);
        }
        if (classes.get(className).size() > 1) {
            throw new ClassDuplicatedException(className);
        }
        int depth = 0;
        for (String classId = classes.get(className).get(0);
             generalize.get(classId).size() != 0; classId = generalize.get(classId).get(0)) {
            depth++;
        }
        return depth;
    }
}