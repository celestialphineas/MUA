package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAeval extends BuiltInOperation {
    public MUAeval() {
        name = "eval";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject result = expr.objectList.get(1);
        StackTrace.getInstance().pop();
        // Eval
        // Todo: Eval recursively
        if(result instanceof ExprListObject) {
            ((ExprListObject)result).evalExpr();
            return ((ExprListObject)result).getReturnVal();
        } else return result;
    }
}
