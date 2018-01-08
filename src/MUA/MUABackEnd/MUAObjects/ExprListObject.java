package MUABackEnd.MUAObjects;

import MUABackEnd.MUAObjects.MUANamespace.GlobalNamespace;
import MUABackEnd.MUAObjects.MUANamespace.Namespace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

// An ExprList is a sequence of MUA objects, with its first element an operator head
// and arguments followed
public class ExprListObject implements MUAObject {
    private static boolean lispForm = false;
    private static int namespaceSerial = 1;
    public Namespace namespace = GlobalNamespace.getInstance();
    public List<MUAObject> objectList;
    private MUAObject returnVal = null;
    public ExprListObject() {
        objectList = new LinkedList<>();
    }
    public ExprListObject(ExprListObject that) {
        if(that.namespace == GlobalNamespace.getInstance()) {
            this.namespace = GlobalNamespace.getInstance();
        } else {
            this.namespace = that.namespace.clone(that.namespace.getName() + "Dup");
        }
        this.objectList = new LinkedList<>(that.objectList);
    }

    public MUAObject getReturnVal() { return this.returnVal; }
    public MUAObject setReturnVal(MUAObject that) { return this.returnVal = that; }

    public String getHeadName() {
        if(!objectList.isEmpty() && objectList.get(0) instanceof OperationObject)
            return ((OperationObject)(objectList.get(0))).getName();
        else return "";
    }
    public static void setLispForm()    { lispForm = true; }
    public static void unsetLispForm()  { lispForm = false; }

    // Set only when the sublist wishes to block the parent from further
    // evaluation.
    private boolean evalDone = false;
    // Operations on the evalDone flag
    public void setEvalDone()   { evalDone = true; }
    public void unsetEvalDone() { evalDone = false; }
    public boolean isEvalDone() { return evalDone; }


    // Allocate namespace for the exprList
    public Namespace allocateNamespace(Namespace parent) {
        this.namespace = new Namespace("localNamespace" + namespaceSerial, parent);
        namespaceSerial++;
        rebindChildrenNamespace();
        return this.namespace;
    }

    private void rebindChildrenNamespace() {
        for(MUAObject obj : objectList) {
            if(obj instanceof ExprListObject) {
                if(((ExprListObject)obj).objectList.size() > 0) {
                    MUAObject op = (((ExprListObject)obj).objectList.get(0));
                    if(op != null
                            && op instanceof BuiltInOperation
                            && op.toString().equals("list")) {
                        ((ExprListObject) obj).allocateNamespace(this.namespace);
                    } else {
                        ((ExprListObject) obj).namespace = this.namespace;
                        ((ExprListObject) obj).rebindChildrenNamespace();
                    }
                } else {
                    ((ExprListObject) obj).namespace = this.namespace;
                    ((ExprListObject) obj).rebindChildrenNamespace();
                }
            }
        }
    }

    // This function clears recursively
    public void clear() {
        namespace.clear();
        for(MUAObject obj : objectList) {
            if(obj instanceof ExprListObject) {
                ((ExprListObject) obj).clear();
            }
        }
    }

    // The evalExpr() method
    public void evalExpr() throws MUAStackOverflowException, MUARuntimeException {
        // Create a copy of the objectList
        List<MUAObject> localObjectList = new LinkedList<>(objectList);
        ListIterator<MUAObject> firstIterator = localObjectList.listIterator();
        firstIterator.next();
        MUAObject firstElement = localObjectList.get(0);
        if(firstElement instanceof DumbHeadObject) {
            String firstName = ((DumbHeadObject) firstElement).getVal();
            // Bind the first element
            firstElement = namespace.getParent().find(firstName);
            // Test use:
            // System.out.println("First element: " + firstElement);
            if(firstElement instanceof BuiltInOperation) {
                firstIterator.set(firstElement);
            } else if(firstElement instanceof ExprListObject) {
                try {
                    firstElement = new CustomOperation((ExprListObject)firstElement);
                } catch (UnOperationizableListException e) {
                    MUAErrorMessage.error(ErrorStringResource.mua_custom_operation,
                        ErrorStringResource.not_operationizable, firstName);
                    throw new MUARuntimeException();
                }
                firstIterator.set(firstElement);
            } else {
                MUAErrorMessage.error(ErrorStringResource.mua_custom_operation,
                    ErrorStringResource.not_operationizable, firstName);
                throw new MUARuntimeException();
            }
        }

        OperationObject operation = (OperationObject) localObjectList.get(0);
        if(operation == null) {
            return;
        }
        setReturnVal(operation.getResult(this));
    }

    // Implements MUAObject
    @Override
    public boolean isAtomic() { return false; }
    @Override
    public String typeName()  { return "expressionList"; }
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        boolean isList = false;
        if(lispForm) {
            buffer.append("( ");
            for(MUAObject object : objectList) {
                buffer.append(object.toString());
                buffer.append(' ');
            }
            buffer.append(')');
            return buffer.toString();
        } else {
            for(MUAObject object : objectList) {
                if(object == null) continue;
                if(object.toString().equals("list")) {
                    isList = true;
                    buffer.append("[ ");
                    continue;
                }
                buffer.append(object.toString());
                buffer.append(' ');
            }
            if(isList) buffer.append("]");
        }
        return buffer.toString();
    }
    @Override
    public String toMUAExprString() {
        StringBuilder buffer = new StringBuilder();
        boolean isList = false;
        for(MUAObject object : objectList) {
            if(object.toString().equals("list")) {
                isList = true;
                buffer.append("[ ");
                continue;
            }
            buffer.append(object.toMUAExprString());
            buffer.append(' ');
        }
        if(isList) buffer.append("]");
        return buffer.toString();
    }
}