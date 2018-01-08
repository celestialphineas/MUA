package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAIO.MUAIO;

public class MUAprint extends BuiltInOperation {
    public MUAprint() {
        name = "print";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject result = expr.objectList.get(1);
        // Eval
        if(result instanceof ExprListObject) {
            ((ExprListObject)result).evalExpr();
            result = ((ExprListObject)result).getReturnVal();
        }
        MUAIO.getInstance().out.println(result);
        StackTrace.getInstance().pop();
        return null;
    }
}
