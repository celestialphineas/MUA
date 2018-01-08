package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAif extends BuiltInOperation {
    public MUAif() {
        name = "if";
        argc = 3;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr.objectList.get(1);
        MUAObject obj2 = expr.objectList.get(2);
        MUAObject obj3 = expr.objectList.get(3);
        MUAObject result;
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof BooleanObject)) {
            MUAErrorMessage.error(ErrorStringResource.operation_if,
                    ErrorStringResource.incompatible_type,
                    obj1.typeName() + ", " + obj2.typeName() + ", " + obj3.typeName());
            throw new MUARuntimeException();
        }
        // if true eval obj2
        if(((BooleanObject) obj1).getVal()) {
            if(obj2 instanceof ExprListObject) {
                ((ExprListObject) obj2).evalExpr();
                if(((ExprListObject) obj2).isEvalDone()) {
                    expr.setEvalDone();
                }
                obj2 = ((ExprListObject) obj2).getReturnVal();
            }
            result = obj2;
        } else {
            if(obj3 instanceof ExprListObject) {
                ((ExprListObject) obj3).evalExpr();
                if(((ExprListObject) obj3).isEvalDone()) {
                    expr.setEvalDone();
                }
                obj3 = ((ExprListObject) obj3).getReturnVal();
            }
            result = obj3;
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
