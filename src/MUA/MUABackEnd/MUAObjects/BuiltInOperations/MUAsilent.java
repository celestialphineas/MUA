package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAsilent extends BuiltInOperation {
    public MUAsilent() {
        name = "silent";
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
        return null;
    }
}
