package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUABackEnd.StackTrace;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAfromunicode extends BuiltInOperation {
    public MUAfromunicode() {
        name = "fromunicode";
        argc = 1;
    }
    @Override
    public MUAObject getResult(ExprListObject expr)
        throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr.objectList.get(1);
        char result = 0;
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof NumObject)) {
            MUAErrorMessage.error(ErrorStringResource.operation_unicode,
                    ErrorStringResource.incompatible_type, obj1.typeName());
            throw new MUARuntimeException();
        }
        result = (char)((NumObject) obj1).getVal();
        StackTrace.getInstance().pop();
        return new WordObject(Character.toString(result));
    }
}