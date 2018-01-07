package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAunicode extends BuiltInOperation {
    public MUAunicode() {
        name = "unicode";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
        throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr.objectList.get(1);
        int result = 0;
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof WordObject)) {
            MUAErrorMessage.error(ErrorStringResource.operation_unicode,
                    ErrorStringResource.incompatible_type, obj1.typeName());
            throw new MUARuntimeException();
        }
        if(!((WordObject) obj1).getVal().isEmpty()) {
            result = (int)((WordObject) obj1).getVal().charAt(0);
        }
        StackTrace.getInstance().pop();
        return new NumObject(result);
    }
}