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
            String type1 = "null", type2 = "null";
            if(obj1 != null) type1 = obj1.typeName();
            if(obj2 != null) type2 = obj2.typeName();
            MUAErrorMessage.error(ErrorStringResource.operation_make,
                ErrorStringResource.incompatible_type,
                type1+ ", " + type2);
            throw new MUARuntimeException();
        }
        MUAObject result = obj2;
        if(obj2 instanceof ExprListObject) {
            MUAObject head = ((ExprListObject)obj2).objectList.get(0);
            if(!head.toString().equals("list")) {
                ((ExprListObject)obj2).evalExpr();
                result = ((ExprListObject)obj2).getReturnVal();
            }
        }
        // Binding
        if(result != null) {
            expr_.namespace.set(((WordObject)obj1).getVal(), result);
        } else {
            expr_.namespace.unset(((WordObject)obj1).getVal());
        }
        StackTrace.getInstance().pop();
        return result;
    }
}
