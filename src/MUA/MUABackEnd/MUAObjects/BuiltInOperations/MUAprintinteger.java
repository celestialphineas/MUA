package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAIO.MUAIO;

public class MUAprintinteger extends BuiltInOperation {
    public MUAprintinteger() {
        name = "printinteger";
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
        if(result instanceof NumObject) {
            MUAIO.getInstance().out.println((int)((NumObject)result).getVal());
        } else {
            MUAIO.getInstance().out.println(result);
        }
        StackTrace.getInstance().pop();
        return null;
    }
}
