package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAiff extends BuiltInOperation {
    public MUAiff() {
        name = "iff";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        expr.unsetEvalDone();
        StackTrace.getInstance().pop();
        MUAObject result = expr.objectList.get(1);
        // Eval
        if(expr.namespace.isTestFalse()) {
            expr.setEvalDone();
            expr.namespace.clearTest();
            if(result instanceof ExprListObject) {
                ((ExprListObject)result).evalExpr();
                return ((ExprListObject)result).getReturnVal();
            } else return result;
        }
        return null;
    }
}
