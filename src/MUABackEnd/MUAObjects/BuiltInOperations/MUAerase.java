package MUABackEnd.MUAObjects.BuiltInOperations;

import MUABackEnd.MUAObjects.*;
import MUAMessageUtil.ErrorStringResource;
import MUAMessageUtil.MUAErrorMessage;

public class MUAerase extends BuiltInOperation {
    public MUAerase() {
        name = "erase";
        argc = 1;
    }
    @Override
    // The make operation is lazy, it works in "set delay" manner
    public MUAObject getResult(ExprListObject expr_)
    throws MUAStackOverflowException, MUARuntimeException {
        StackTrace.getInstance().push(name);
        MUAObject obj1 = expr_.objectList.get(1);
        // Eval the first slot
        if(obj1 instanceof ExprListObject) {
            ((ExprListObject) obj1).evalExpr();
            obj1 = ((ExprListObject) obj1).getReturnVal();
        }
        if(!(obj1 instanceof WordObject)) {
            String type1 = "null";
            if(obj1 != null) type1 = obj1.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_erase,
                ErrorStringResource.incompatible_type, type1);
            throw new MUARuntimeException();
        }
        expr_.namespace.unset(((WordObject)obj1).getVal());
        StackTrace.getInstance().pop();
        return null;
    }
}
