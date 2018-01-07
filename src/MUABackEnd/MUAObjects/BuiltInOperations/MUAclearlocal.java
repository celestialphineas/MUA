package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUACore;
import MUABackEnd.MUAObjects.*;

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
        expr.namespace.clear();
        return null;
    }
}
