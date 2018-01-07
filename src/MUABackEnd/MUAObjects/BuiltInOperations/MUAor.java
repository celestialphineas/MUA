package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAor extends BuiltInOperation {
    public MUAor() {
        name = "or";
        argc = 2;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        MUAObject obj2 = expr_.objectList.get(2);
        // Eval 1
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject)obj1).evalExpr();
            obj1 = ((ExprListObject)obj1).getReturnVal();
        }
        // Short circuit
        if(obj1 instanceof BooleanObject && ((BooleanObject) obj1).getVal() == true) {
            StackTrace.getInstance().pop();
            return new BooleanObject(true);
        }
        // Eval 2
        if(obj2 instanceof ExprListObject) {
            ((ExprListObject)obj2).evalExpr();
            obj2 = ((ExprListObject)obj2).getReturnVal();
        }
        if(obj1 instanceof BooleanObject && obj2 instanceof BooleanObject) {
            boolean result
                = ((BooleanObject)obj1).getVal() | ((BooleanObject)obj2).getVal();
            return new BooleanObject(result);
        }
        String type1 = "null", type2 = "null";
        if(obj1 != null) type1 = obj1.typeName();
        if(obj2 != null) type2 = obj2.typeName();
        MUAErrorMessage.error(ErrorStringResource.operation_or,
                ErrorStringResource.incompatible_type,
                type1 + ", " + type2);
        StackTrace.getInstance().pop();
        throw new MUARuntimeException();
    }
}
