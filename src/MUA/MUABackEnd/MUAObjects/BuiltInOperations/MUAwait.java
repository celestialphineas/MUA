package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAwait extends BuiltInOperation {
    public MUAwait() {
        name = "wait";
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
        if(obj1 instanceof NumObject) {
            double millis = ((NumObject)obj1).getVal();
            try {
                Thread.currentThread().sleep((long)millis);
            } catch (Exception e) {
                MUAErrorMessage.error(ErrorStringResource.operation_wait,
                        ErrorStringResource.unknow_internal_error, e.getMessage());
            }
            return null;
        }
        String type1 = "null";
        if(obj1 != null) type1 = obj1.typeName();
        MUAErrorMessage.error(ErrorStringResource.operation_wait,
                ErrorStringResource.incompatible_type,
                type1);
        StackTrace.getInstance().pop();
        throw new MUARuntimeException();
    }
}
