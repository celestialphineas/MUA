package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAnot extends BuiltInOperation {
    public MUAnot() {
        name = "not";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject)obj1).evalExpr();
            obj1 = ((ExprListObject)obj1).getReturnVal();
        }
        if(obj1 instanceof BooleanObject) {
            boolean result = !((BooleanObject)obj1).getVal();
            return new BooleanObject(result);
        }
        String type1 = "null";
        if(obj1 != null) type1 = obj1.typeName();
        MUAErrorMessage.error(ErrorStringResource.operation_not,
                ErrorStringResource.incompatible_type, type1);
        StackTrace.getInstance().pop();
        throw new MUARuntimeException();
    }
}
