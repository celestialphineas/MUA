package MUABackEnd.MUAObjects;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUANamespace.Namespace;
import java.util.LinkedList;
import java.util.List;

public class ExprListObject implements MUAObject {
    private Namespace namespace = GlobalNamespace.getInstance();
    private MUAObject returnVal = null;
    private boolean evalDone = false;
    final List<MUAObject> objectList = new LinkedList<>();
    public ExprListObject() { }
    public void evalExpr() {
        // TODO
    }
    @Override
    public boolean isAtomic() { return false; }
}