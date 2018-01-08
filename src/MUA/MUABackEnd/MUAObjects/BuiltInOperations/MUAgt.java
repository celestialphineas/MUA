package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAgt extends BuiltInOperation {
    public MUAgt() {
        name = "gt";
        argc = 2;
    }
    @Override
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        MUAObject obj2 = expr_.objectList.get(2);
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject)obj1).evalExpr();
            obj1 = ((ExprListObject)obj1).getReturnVal();
        }
        if(obj2 instanceof ExprListObject) {
            ((ExprListObject)obj2).evalExpr();
            obj2 = ((ExprListObject)obj2).getReturnVal();
        }
        if(obj1 instanceof NumObject && obj2 instanceof NumObject) {
            boolean result
                = ((NumObject)obj1).getVal() > ((NumObject)obj2).getVal();
            return new BooleanObject(result);
        } else if(obj1 instanceof WordObject && obj2 instanceof WordObject) {
            boolean result
                = (((WordObject)obj1).getVal().compareTo(((WordObject)obj2).getVal()) > 0);
            return new BooleanObject(result);
        }

        String type1 = "null", type2 = "null";
        if(obj1 != null) type1 = obj1.typeName();
        if(obj2 != null) type2 = obj2.typeName();
        MUAErrorMessage.error(ErrorStringResource.operation_gt,
                ErrorStringResource.incompatible_type,
                type1 + ", " + type2);
        StackTrace.getInstance().pop();
        throw new MUARuntimeException();
    }
}
