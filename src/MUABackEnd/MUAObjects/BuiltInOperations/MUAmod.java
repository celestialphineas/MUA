package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAmod extends BuiltInOperation {
    public MUAmod() {
        name = "mod";
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
            double result
                = (int)(((NumObject)obj1).getVal() + 0.5) %
                    (int)(((NumObject)obj2).getVal() + 0.5);
            return new NumObject(result);
        }
        String type1 = "unknown", type2 = "unknown";
        if(obj1 != null) type1 = obj1.typeName();
        if(obj2 != null) type2 = obj2.typeName();
        MUAErrorMessage.error(ErrorStringResource.operation_add,
                ErrorStringResource.incompatible_type,
                type1 + ", " + type2);
        StackTrace.getInstance().pop();
        throw new MUARuntimeException();
    }
}
