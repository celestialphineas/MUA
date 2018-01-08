package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUACore;
import MUABackEnd.MUANamespace.GlobalNamespace;
import MUABackEnd.MUAObjects.*;

import java.util.Map;

public class MUAclearlocal extends BuiltInOperation {
    public MUAclearlocal() {
        name = "clearlocal";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        if(expr.namespace != GlobalNamespace.getInstance()) {
            for(Map.Entry<String, MUAObject> entry : expr.namespace.getMap().entrySet()) {
                if(!(entry.getValue() instanceof BuiltInOperation)) {
                    expr.namespace.getMap().remove(entry.getKey());
                }
            }
        }
        return null;
    }
}
