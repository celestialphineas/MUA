package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAisword extends BuiltInOperation {
    public MUAisword() {
        name = "isword";
        argc = 1;
    }
    @Override
    // The make operation is lazy, it works in "set delay" manner
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        // Eval the first slot
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        StackTrace.getInstance().pop();
        if(obj1 instanceof WordObject) {
            return new BooleanObject(true);
        }
        return new BooleanObject(false);
    }
}
