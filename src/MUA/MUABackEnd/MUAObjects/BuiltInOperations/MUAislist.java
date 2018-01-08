package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;

public class MUAislist extends BuiltInOperation {
    public MUAislist() {
        name = "islist";
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
        if(obj1 instanceof ExprListObject
            && ((ExprListObject)obj1).objectList != null
            && ((ExprListObject)obj1).objectList.get(0) != null
            && ((ExprListObject)obj1).objectList.get(0) instanceof BuiltInOperation
            && ((BuiltInOperation)((ExprListObject)obj1).objectList.get(0)).getName() == "list") {
                return new BooleanObject(true);
        }
        return new BooleanObject(false);
    }
}
