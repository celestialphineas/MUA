package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAmake extends BuiltInOperation {
    public MUAmake() {
        name = "make";
        argc = 2;
    }
    @Override
    // The make operation is lazy, it works in "set delay" manner
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        MUAObject obj2 = expr_.objectList.get(2);
        // Eval the first slot
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof WordObject)) {
            MUAErrorMessage.error(ErrorStringResource.operation_make,
                ErrorStringResource.incompatible_type,
                obj1.typeName() + ", " + obj2.typeName());
            throw new MUARuntimeException();
        }
        // Binding
        expr_.namespace.set(((WordObject)obj1).getVal(), obj2);
        StackTrace.getInstance().pop();
        return obj2;
    }
}
