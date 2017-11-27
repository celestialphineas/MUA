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
        // Eval
        if(result instanceof ExprListObject) {
            // Eval one more time for operation "thing"
            if(((ExprListObject) result).objectList.get(0).toString().equals("thing")) {
                ((ExprListObject) result).evalExpr();
                result = ((ExprListObject) result).getReturnVal();
            }
            if(result instanceof ExprListObject) {
                ((ExprListObject)result).evalExpr();
                result = ((ExprListObject)result).getReturnVal();
            }
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
