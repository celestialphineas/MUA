package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAhold extends BuiltInOperation {
    public MUAhold() {
        name = "hold";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
            throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject result = expr.objectList.get(1);
        StackTrace.getInstance().pop();
        return result;
    }
}
