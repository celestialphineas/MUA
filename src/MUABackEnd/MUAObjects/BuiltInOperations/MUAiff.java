package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAiff extends BuiltInOperation {
    public MUAiff() {
        name = "iff";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        expr.setEvalDone();
        StackTrace.getInstance().pop();
        if(expr.objectList.size() <= 1) {
            return null;
        }
        MUAObject result = expr.objectList.get(1);
        // Eval
        if(expr.isTestFalse()) {
            if(result instanceof ExprListObject) {
                ((ExprListObject)result).evalExpr();
                return ((ExprListObject)result).getReturnVal();
            } else return result;
        }
        return null;
    }
}
