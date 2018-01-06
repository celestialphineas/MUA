package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUAObjects.*;

import java.util.Map;

public class MUAexposeall extends BuiltInOperation {
    public MUAexposeall() {
        name = "exposeall";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        if(expr.namespace != expr.namespace.getParent()) {
            for(Map.Entry<String, MUAObject> entry : expr.namespace.getMap().entrySet()) {
                GlobalNamespace.getInstance().set(entry.getKey(), entry.getValue());
            }
        }
        StackTrace.getInstance().pop();
        return null;
    }
}
