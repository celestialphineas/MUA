package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAstop extends BuiltInOperation {
    public MUAstop() {
        name = "stop";
        argc = 0;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException{
        StackTrace.getInstance().push(name);
        expr.setEvalDone();
        StackTrace.getInstance().pop();
        return null;
    }
}
