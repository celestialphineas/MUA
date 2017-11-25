package MUABackEnd.MUAObjects;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUANamespace.Namespace;
import java.util.LinkedList;
import java.util.List;

// An ExprList is a sequence of MUA objects, with its first element an operator head
// and arguments followed
public class ExprListObject implements MUAObject {
    static private int namespaceSerial = 1;
    public Namespace namespace = GlobalNamespace.getInstance();
    private MUAObject returnVal = null;
    private boolean evalDone = false;
    final public List<MUAObject> objectList = new LinkedList<>();
    public ExprListObject() { }
    public MUAObject getReturnVal() { return returnVal; }
    public String getHeadName() {
        if(!objectList.isEmpty() && objectList.get(0) instanceof OperationObject)
            return ((OperationObject)(objectList.get(0))).getName();
        else return "";
    }

    // Operations on the evalDone flag
    public void setEvalDone()   { evalDone = true; }
    public void unsetEvalDone() { evalDone = false; }
    public boolean isEvalDone() { return evalDone; }
    // Allocate namespace for the exprList
    public Namespace allocateNamespace(Namespace parent) {
        if(namespace != parent) {
            namespace = new Namespace("LocalNamespace" + namespaceSerial, parent);
            namespaceSerial++;
        }
        return namespace;
    }
    // The evalExpr() method
    public void evalExpr() {
        // TODO
    }

    // Implements MUAObject
    @Override
    public boolean isAtomic() { return false; }
    @Override
    public String typeName()  { return "expressionList"; }
    @Override
    public String toString() {
        StringBuilder buffer = new StringBuilder();
        buffer.append('(');
        for(MUAObject object : objectList) {
            buffer.append(object.toString());
            buffer.append(' ');
        }
        buffer.append(')');
        return buffer.toString();
    }
}