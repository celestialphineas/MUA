package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAtest extends BuiltInOperation {
    public MUAtest() {
        name = "test";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        StackTrace.getInstance().pop();
        if(expr.objectList.size() <= 1) {
            return null;
        }
        MUAObject result = expr.objectList.get(1);
        // Eval
        if(result instanceof ExprListObject) {
            ((ExprListObject)result).evalExpr();
            result = ((ExprListObject)result).getReturnVal();
        }
        if(result instanceof BooleanObject) {
            if(((BooleanObject) result).getVal()) {
                expr.namespace.setTest();
            } else {
                expr.namespace.unsetTest();
            }
            return result;
        }
        String typename = "null";
        if(result != null) typename = result.typeName();
        MUAErrorMessage.warn(ErrorStringResource.operation_test,
            ErrorStringResource.incompatible_type, typename);
        return result;
    }
}
