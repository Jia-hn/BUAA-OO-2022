import com.oocourse.uml3.interact.common.AttributeClassInformation;
import com.oocourse.uml3.interact.exceptions.user.ClassDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.ClassNotFoundException;
import com.oocourse.uml3.interact.exceptions.user.MethodDuplicatedException;
import com.oocourse.uml3.interact.exceptions.user.MethodWrongTypeException;
import com.oocourse.uml3.interact.exceptions.user.UmlRule001Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule002Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule003Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule004Exception;
import com.oocourse.uml3.interact.exceptions.user.UmlRule005Exception;
import com.oocourse.uml3.models.common.Direction;
import com.oocourse.uml3.models.common.NameableType;
import com.oocourse.uml3.models.common.NamedType;
import com.oocourse.uml3.models.common.ReferenceType;
import com.oocourse.uml3.models.common.Visibility;
import com.oocourse.uml3.models.elements.UmlAssociation;
import com.oocourse.uml3.models.elements.UmlAssociationEnd;
import com.oocourse.uml3.models.elements.UmlAttribute;
import com.oocourse.uml3.models.elements.UmlClass;
import com.oocourse.uml3.models.elements.UmlClassOrInterface;
import com.oocourse.uml3.models.elements.UmlElement;
import com.oocourse.uml3.models.elements.UmlGeneralization;
import com.oocourse.uml3.models.elements.UmlInterface;
import com.oocourse.uml3.models.elements.UmlInterfaceRealization;
import com.oocourse.uml3.models.elements.UmlOperation;
import com.oocourse.uml3.models.elements.UmlParameter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class UmlClassModel {
    private final HashMap<String, UmlElement> elements = new HashMap<>();
    private final HashMap<String, ArrayList<String>> classes = new HashMap<>();
    private final HashMap<String, ArrayList<String>> interfaces = new HashMap<>();
    private final HashMap<String, ArrayList<String>> attributes = new HashMap<>();
    private final HashMap<String, HashMap<String, ArrayList<String>>> operations = new HashMap<>();
    private final HashMap<String, ArrayList<String>> parameters = new HashMap<>();
    private final HashMap<String, ArrayList<String>> associations = new HashMap<>();
    private final HashMap<String, ArrayList<String>> generalize = new HashMap<>();
    private final HashMap<String, ArrayList<String>> generalized = new HashMap<>();
    private final HashMap<String, ArrayList<String>> realize = new HashMap<>();
    private final HashMap<String, ArrayList<String>> realized = new HashMap<>();

    public UmlClassModel(UmlElement... elements) {
        for (UmlElement element : elements) {
            this.elements.put(element.getId(), element);
        }
        for (UmlElement element : elements) {
            if (element instanceof UmlClass || element instanceof UmlInterface) {
                attributes.put(element.getId(), new ArrayList<>());
                operations.put(element.getId(), new HashMap<>());
                associations.put(element.getId(), new ArrayList<>());
                generalize.put(element.getId(), new ArrayList<>());
                generalized.put(element.getId(), new ArrayList<>());
                if (element instanceof UmlClass) {
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
            if (element instanceof UmlAttribute && !(this.elements.get(element.getParentId())
                    instanceof com.oocourse.uml3.models.elements.UmlCollaboration)) {
                attributes.get(element.getParentId()).add(element.getId());
            } else if (element instanceof UmlOperation) {
                if (!operations.get(element.getParentId()).containsKey(element.getName())) {
                    operations.get(element.getParentId()).put(element.getName(), new ArrayList<>());
                }
                operations.get(element.getParentId()).get(element.getName()).add(element.getId());
                parameters.put(element.getId(), new ArrayList<>());
            } else if (element instanceof UmlAssociation) {
                UmlAssociationEnd associationEnd1 = (UmlAssociationEnd)
                        this.elements.get(((UmlAssociation) element).getEnd1());
                UmlAssociationEnd associationEnd2 = (UmlAssociationEnd)
                        this.elements.get(((UmlAssociation) element).getEnd2());
                associations.get(associationEnd1.getReference()).add(associationEnd2.getId());
                associations.get(associationEnd2.getReference()).add(associationEnd1.getId());
            } else if (element instanceof UmlGeneralization) {
                UmlGeneralization generalization = (UmlGeneralization) element;
                generalize.get(generalization.getSource()).add(generalization.getTarget());
                generalized.get(generalization.getTarget()).add(generalization.getSource());
            } else if (element instanceof UmlInterfaceRealization) {
                UmlInterfaceRealization interfaceRealization = (UmlInterfaceRealization) element;
                realize.get(interfaceRealization.getSource()).add(interfaceRealization.getTarget());
                realized.get(interfaceRealization.getTarget()).
                        add(interfaceRealization.getSource());
            }
        }
        for (UmlElement element : elements) {
            if (element instanceof UmlParameter) {
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
            for (String parameterId : parameters.get(operationId)) {
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

    public boolean isEmpty(String string) {
        return string == null || string.matches("[ \\t]*");
    }

    public void checkForUml001() throws UmlRule001Exception {
        for (UmlElement element : elements.values()) {
            if (element instanceof UmlClass && isEmpty(element.getName())) {
                throw new UmlRule001Exception();
            }
            if (element instanceof UmlInterface && isEmpty(element.getName())) {
                throw new UmlRule001Exception();
            }
            if (element instanceof UmlOperation && isEmpty(element.getName())) {
                throw new UmlRule001Exception();
            }
            if (element instanceof UmlAttribute && isEmpty(element.getName()) &&
                    (elements.get(element.getParentId()) instanceof UmlClass ||
                    elements.get(element.getParentId()) instanceof UmlInterface)) {
                throw new UmlRule001Exception();
            }
            if (element instanceof UmlParameter && isEmpty(element.getName()) &&
                    !((UmlParameter) element).getDirection().equals(Direction.RETURN)) {
                throw new UmlRule001Exception();
            }
        }
    }

    public void checkForUml002() throws UmlRule002Exception {
        HashSet<AttributeClassInformation> attributeClassInformation = new HashSet<>();
        HashMap<String, HashSet<String>> names = new HashMap<>();
        for (String classId : attributes.keySet()) {
            if (!(elements.get(classId) instanceof UmlClass)) {
                continue;
            }
            names.put(classId, new HashSet<>());
            for (String attributeId : attributes.get(classId)) {
                if (isEmpty(elements.get(attributeId).getName())) {
                    continue;
                }
                if (names.get(classId).contains(elements.get(attributeId).getName())) {
                    attributeClassInformation.add(new AttributeClassInformation(elements.
                            get(attributeId).getName(), elements.get(classId).getName()));
                } else {
                    names.get(classId).add(elements.get(attributeId).getName());
                }
            }
        }
        for (String classId : associations.keySet()) {
            if (!(elements.get(classId) instanceof UmlClass)) {
                continue;
            }
            for (String associationEndId : associations.get(classId)) {
                if (isEmpty(elements.get(associationEndId).getName())) {
                    continue;
                }
                if (names.get(classId).contains(elements.get(associationEndId).getName())) {
                    attributeClassInformation.add(new AttributeClassInformation(elements.
                            get(associationEndId).getName(), elements.get(classId).getName()));
                } else {
                    names.get(classId).add(elements.get(associationEndId).getName());
                }
            }
        }
        if (!attributeClassInformation.isEmpty()) {
            throw new UmlRule002Exception(attributeClassInformation);
        }
    }

    public void checkForUml003() throws UmlRule003Exception {
        HashSet<UmlClassOrInterface> classOrInterfaces = new HashSet<>();
        ArrayList<String> classOrInterfaceIds = new ArrayList<>(generalize.keySet());
        for (String classOrInterfaceId1 : classOrInterfaceIds) {
            HashSet<String> visitedId = new HashSet<>();
            LinkedList<String> queue = new LinkedList<>();
            queue.add(classOrInterfaceId1);
            visitedId.add(classOrInterfaceId1);
            loop:
            while (!queue.isEmpty()) {
                String classOrInterfaceId2 = queue.element();
                queue.remove();
                for (String classOrInterfaceId3 : generalize.get(classOrInterfaceId2)) {
                    if (!visitedId.contains(classOrInterfaceId3)) {
                        queue.add(classOrInterfaceId3);
                        visitedId.add(classOrInterfaceId3);
                    }
                    if (classOrInterfaceId3.equals(classOrInterfaceId1)) {
                        classOrInterfaces.add((UmlClassOrInterface)
                                elements.get(classOrInterfaceId1));
                        break loop;
                    }
                }
            }
        }
        if (!classOrInterfaces.isEmpty()) {
            throw new UmlRule003Exception(classOrInterfaces);
        }
    }

    public void checkForUml004() throws UmlRule004Exception {
        HashSet<UmlClassOrInterface> classOrInterfaces = new HashSet<>();
        ArrayList<String> classOrInterfaceIds = new ArrayList<>(generalize.keySet());
        for (String classOrInterfaceId1 : classOrInterfaceIds) {
            HashSet<String> visitedId = new HashSet<>();
            LinkedList<String> queue = new LinkedList<>();
            queue.add(classOrInterfaceId1);
            visitedId.add(classOrInterfaceId1);
            loop:
            while (!queue.isEmpty()) {
                String classOrInterfaceId2 = queue.element();
                queue.remove();
                for (String classOrInterfaceId3 : generalize.get(classOrInterfaceId2)) {
                    if (visitedId.contains(classOrInterfaceId3)) {
                        classOrInterfaces.add((UmlClassOrInterface)
                                elements.get(classOrInterfaceId1));
                        break loop;
                    }
                    queue.add(classOrInterfaceId3);
                    visitedId.add(classOrInterfaceId3);
                }
            }
        }
        if (!classOrInterfaces.isEmpty()) {
            throw new UmlRule004Exception(classOrInterfaces);
        }
    }

    public void checkForUml005() throws UmlRule005Exception {
        for (UmlElement element : elements.values()) {
            if (element instanceof UmlAttribute &&
                    elements.get(element.getParentId()) instanceof UmlInterface &&
                    !((UmlAttribute) element).getVisibility().equals(Visibility.PUBLIC)) {
                throw new UmlRule005Exception();
            }
        }
    }
}
