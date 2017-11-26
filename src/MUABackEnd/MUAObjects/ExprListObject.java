package MUABackEnd.MUAObjects;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUANamespace.Namespace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

// An ExprList is a sequence of MUA objects, with its first element an operator head
// and arguments followed
public class ExprListObject implements MUAObject {
    private static int namespaceSerial = 1;
    public Namespace namespace = GlobalNamespace.getInstance();
    private MUAObject returnVal = null;
    // Set only when the sublist wishes to block the parent from further
    // evaluation.
    private boolean evalDone = false;
    // A boolean flag for "ift" and "iff"
    private int testReturnVal = -1;
    public List<MUAObject> objectList;
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
    public MUAObject getReturnVal() {
        return returnVal;
    }
    public String getHeadName() {
        if(!objectList.isEmpty() && objectList.get(0) instanceof OperationObject)
            return ((OperationObject)(objectList.get(0))).getName();
        else return "";
    }

    // Operations on the evalDone flag
    public void setEvalDone()   { evalDone = true; }
    public void unsetEvalDone() { evalDone = false; }
    public boolean isEvalDone() { return evalDone; }
    // Set the testReturnVal flag
    public void setTest()           { testReturnVal = 1; }
    public void unsetTest()         { testReturnVal = 0; }
    public void clearTest()         { testReturnVal = -1; }
    public boolean isTestTrue()     { return testReturnVal == 1; }
    public boolean isTestFalse()    { return testReturnVal == 0; }
    // Allocate namespace for the exprList
    public Namespace allocateNamespace(Namespace parent) {
        namespace = new Namespace("localNamespace" + namespaceSerial, parent);
        namespaceSerial++;
        return namespace;
    }
    // The evalExpr() method
    public void evalExpr() throws MUAStackOverflowException, MUARuntimeException {
        // TODO
        ListIterator<MUAObject> firstIterator = objectList.listIterator();
        firstIterator.next();
        MUAObject firstElement = objectList.get(0);
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

        OperationObject operation = (OperationObject) objectList.get(0);
        if(operation == null) {
            return;
        }
        returnVal = operation.getResult(this);
    }

    // Implements MUAObject
    @Override
    public boolean isAtomic() { return false; }
    @Override
    public String typeName()  { return "expressionList"; }
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append("( ");
        for(MUAObject object : objectList) {
            buffer.append(object.toString());
            buffer.append(' ');
        }
        buffer.append(')');
        return buffer.toString();
    }
}