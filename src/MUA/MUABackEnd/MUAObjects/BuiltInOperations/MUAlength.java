package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;

public class MUAlength extends BuiltInOperation {
    public MUAlength() {
        name = "length";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr.objectList.get(1);
        int count = 1;
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(obj1 instanceof ExprListObject) {
            count = ((ExprListObject) obj1).objectList.size() - 1;
        } else if(obj1 instanceof WordObject) {
            count = ((WordObject) obj1).getVal().length();
        }
        StackTrace.getInstance().pop();
        return new NumObject(count);
    }
}
